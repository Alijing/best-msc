import type {RouteMeta, RouteRecordNormalized, RouteRecordRaw, RouterOptions} from "vue-router";
import {isUrl} from "@/utils/is";
import {createRouter, createWebHashHistory} from "vue-router";
import {omit, cloneDeep} from "lodash-es"

const modules = import.meta.glob('../views/**/*.{vue,tsx}')

/**
 * Layout
 */
export const Layout = () => import('@/layout/Layout.vue');

export const getParentLayout = () => {
    return () => new Promise((resolve => {
        resolve({name: 'ParentLayout'})
    }))
}

/**
 * 前端控制路由生成
 */
export const generateRoutesFn1 = (routes: AppRouteRecordRaw[], keys: string[], basePath = '/'): AppRouteRecordRaw[] => {
    const res: AppRouteRecordRaw[] = []
    for (const route of routes) {
        const meta = route.meta as RouteMeta;
        // skip some route
        if (meta.hidden && !meta.canTo) {
            continue
        }
        let data: Nullable<AppRouteRecordRaw> = null
        let onlyOneChild: Nullable<string> = null
        if (route.children && 1 === route.children.length && !meta.alwaysShow) {
            onlyOneChild = (
                isUrl(route.children[0].path) ?
                    route.children[0].path : pathResolve(pathResolve(basePath, route.path), route.children[0].path)
            ) as string
        }

        // 开发者可以根据实际情况进行扩展
        for (const item of keys) {
            // 通过路径去匹配
            if (isUrl(item) && (onlyOneChild === item || route.path === item)) {
                data = Object.assign({}, route)
            } else {
                const routePath = onlyOneChild ?? pathResolve(basePath, route.path)
                if (routePath === item || meta.followAuth === item) {
                    data = Object.assign({}, route)
                }
            }
        }

        // recursive child routes
        if (route.children && data) {
            data.children = generateRoutesFn1(route.children, keys, pathResolve(basePath, data.path))
        }
        if (data) {
            res.push(data)
        }
    }
    return res
}

/**
 * 后端控制路由生成
 */
export const generateRoutesFn2 = (routes: AppCustomRouteRecordRaw[]): AppRouteRecordRaw[] => {
    const res: AppRouteRecordRaw[] = []
    for (const route of routes) {
        const data: AppRouteRecordRaw = {
            path: route.path,
            name: route.name,
            redirect: route.redirect,
            meta: route.meta,
        } as AppRouteRecordRaw

        if (route.component) {
            const comModule = modules[`../${route.component}.vue`] || modules[`../${route.component}.tsx`]
            const component = route.component as string
            if (!comModule && !component.includes('#')) {
                console.error(`未找到${route.component}.vue文件或${route.component}.tsx文件，请创建`)
            } else {
                // 动态加载路由文件，可根据实际情况进行自定义逻辑
                data.component = component === '#' ? Layout : component.includes('##') ? getParentLayout() : comModule
            }
        }

        // recursive child routes
        if (route.children && data) {
            data.children = generateRoutesFn2(route.children)
        }
        if (data) {
            res.push(data)
        }
    }
    return res
}

export const pathResolve = (parentPath: string, path: string) => {
    if (isUrl(path)) {
        return path;
    }
    const childPath = path.startsWith('/') || !path ? path : `/${path}`
    return `${parentPath}${childPath}`.replace(/\/\//g, '/')
}


// 路由降级
export const flatMultiLevelRoutes = (routes: AppRouteRecordRaw[]) => {
    const modules: AppRouteRecordRaw[] = cloneDeep(routes)
    for (let i = 0; i < modules.length; i++) {
        const route = modules[i];
        if (!isMultipleRoute(route)) {
            continue
        }
        promoteRouteLevel(route)
    }
    return modules
}


/**
 * 层级是否大于 2
 *
 * @param route
 */
const isMultipleRoute = (route: AppRouteRecordRaw) => {
    if (!route || !Reflect.has(route, 'children') || !route.children?.length) {
        return false
    }
    const children = route.children;
    let flag = false;
    for (let i = 0; i < children.length; i++) {
        const child = children[i];
        if (child.children?.length) {
            flag = true
            break
        }
    }
    return flag;
}


/**
 * 生成二级路由
 * @param route
 */
const promoteRouteLevel = (route: AppRouteRecordRaw) => {
    let router = createRouter({
        routes: [route as RouteRecordRaw],
        history: createWebHashHistory()
    } as RouterOptions);

    const routes = router.getRoutes();
    addToChildren(routes, route.children || [], route)
    router = null

    route.children = route.children?.map(it => omit(it, 'children'))
}

/**
 * 添加子菜单
 * @param routes
 * @param children
 * @param routeModule
 */
const addToChildren = (routes: RouteRecordNormalized[], children: AppRouteRecordRaw[], routeModule: AppRouteRecordRaw) => {
    for (const child of children) {
        const route = routes.find(it => it.name === child.name);
        if (!route) {
            continue
        }
        routeModule.children = routeModule.children || []
        if (!routeModule.children.find(it => it.name === route.name)) {
            routeModule.children?.push(route as unknown as AppRouteRecordRaw)
        }
        if (child.children?.length) {
            addToChildren(routes, child.children, routeModule)
        }
    }
}