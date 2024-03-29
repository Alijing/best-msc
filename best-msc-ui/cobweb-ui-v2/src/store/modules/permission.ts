import {defineStore} from "pinia";
import {asyncRouterMap, constantRouterMap} from "@/router";
import {store} from "@/store";
import {flatMultiLevelRoutes, generateRoutesFn1, generateRoutesFn2} from "@/utils/routerHelper";
import {cloneDeep} from 'lodash-es'

export interface PermissionState {
    routers: AppRouteRecordRaw[]
    addRouters: AppRouteRecordRaw[]
    isAddRouters: boolean
    menuTabRouters: AppRouteRecordRaw[]
}

export const usePermissionStore = defineStore('permission', {
    state: (): PermissionState => ({
        routers: [],
        addRouters: [],
        isAddRouters: false,
        menuTabRouters: []
    }),
    getters: {
        getRouters(): AppRouteRecordRaw[] {
            return this.routers
        },
        getAddRouters(): AppRouteRecordRaw[] {
            return flatMultiLevelRoutes(cloneDeep(this.addRouters))
        },
        getIsAddRouters(): boolean {
            return this.isAddRouters
        },
        getMenuTabRouters(): AppRouteRecordRaw[] {
            return this.menuTabRouters
        }
    },
    actions: {
        generateRoutes(
            type: 'admin' | 'test' | 'none',
            routers?: AppCustomRouteRecordRaw[] | string[]
        ): Promise<unknown> {
            return new Promise<unknown>((resolve) => {
                let routerMap: AppRouteRecordRaw[] = [];
                if ('admin' === type) {
                    // 模拟后端过滤菜单
                    routerMap = generateRoutesFn2(routers as AppCustomRouteRecordRaw[])
                } else if ('test' === type) {
                    // 模拟前端过滤菜单
                    routerMap = generateRoutesFn1(cloneDeep(asyncRouterMap), routers as AppCustomRouteRecordRaw[])
                } else {
                    // 直接读取静态路由表
                    routerMap = cloneDeep(asyncRouterMap)
                }
                // 动态路由，404 一定要放到最后面
                this.addRouters = routerMap.concat([
                    {
                        path: '/:path(.*)*',
                        redirect: '/404',
                        name: '404Page',
                        meta: {
                            hidden: true,
                            breadcrumbs: false
                        }
                    } as AppRouteRecordRaw
                ])
                // 渲染菜单的所有路由
                this.routers = cloneDeep(constantRouterMap).concat(routerMap)
                resolve()
            })
        },
        setIsAddRouters(state: boolean): void {
            this.isAddRouters = state
        },
        setMenuTabRouters(routers: AppRouteRecordRaw[]): void {
            this.menuTabRouters = routers
        }
    }
})

export const usePermissionStoreWithOut = () => {

    return usePermissionStore(store)
}