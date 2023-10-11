import {usePermissionStoreWithOut} from "@/store/modules/permission"
import {useAppStoreWithOut} from "@/store/modules/app"
import {useStorage} from "@/hooks/web/useStorage"
import {useNProgress} from "@/hooks/web/useNProgress"
import {uswPageLoading} from "@/hooks/web/usePageLoading"
import router from "@/router"
import type {RouteRecordRaw} from "vue-router"
import {useTitle} from "@vueuse/core/index"

const permissionStore = usePermissionStoreWithOut()

const appStore = useAppStoreWithOut()

const {getStorage} = useStorage()

const {start, done} = useNProgress()

const {loadStart, loadDone} = uswPageLoading()

// 不重定向白名单
const whiteList = ['/login',]

router.beforeEach(async (to, from, next) => {
    start()
    loadStart()
    const userInfo = getStorage(appStore.getUserInfo);
    if (!userInfo) {
        if (-1 !== whiteList.indexOf(to.path)) {
            next()
        } else {
            next(`/login?redirect=${to.path}`)
        }
        return
    }

    if ('/login' === to.path) {
        next({path: '/'})
        return
    }

    if (permissionStore.getIsAddRouters) {
        next()
        return
    }

    // 开发者可根据实际情况进行修改
    const roleRouters = getStorage('roleRouters') || []

    // 是否使用动态路由
    if (appStore.getDynamicRouter) {
        'admin' === userInfo.role
            ? await permissionStore.generateRoutes('admin', roleRouters as AppCustomRouteRecordRaw[])
            : await permissionStore.generateRoutes('test', roleRouters as string[])
    } else {
        await permissionStore.generateRoutes('none')
    }

    permissionStore.getAddRouters.forEach(route => {
        // 动态添加可访问路由表
        router.addRoute(route as unknown as RouteRecordRaw)
    })
    const redirectPath = from.query.redirect || to.path
    const redirect = decodeURIComponent(redirectPath as string);
    const nextData = to.path === redirect ? {...to, replace: true} : {path: redirect};
    permissionStore.setIsAddRouters(true)
    next(nextData)

})

router.afterEach((to) => {
    useTitle(to?.meta?.title as string)
    // 结束 Progress
    done()
    loadDone()
})

