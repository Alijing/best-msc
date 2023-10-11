import {createRouter, RouterOptions, createWebHashHistory} from 'vue-router'
import type {App} from 'vue'

import HomeView from '../views/HomeView.vue'
import {Layout} from "@/utils/routerHelper";


export const constantRouterMap: AppRouteRecordRaw[] = [
    {
        path: '/',
        component: Layout,
        redirect: '/level',
        name: 'Root',
        meta: {
            hidden: true
        }
    } as AppRouteRecordRaw,
    {
        path: '/redirect',
        component: Layout,
        name: 'Redirect',
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/redirect/Redirect.vue'),
                name: 'Redirect',
                meta: {}
            }
        ],
        meta: {
            hidden: true,
            noTagsView: true
        }
    } as AppRouteRecordRaw,
    {
        path: '/login',
        component: () => import('@/views/login/Login.vue'),
        name: 'Login',
        meta: {
            hidden: true,
            title: '登录',
            noTagsView: true
        }
    } as AppRouteRecordRaw,
    {
        path: '/404',
        component: () => import('@/views/error/404.vue'),
        name: 'NoFind',
        meta: {
            hidden: true,
            title: '404',
            noTagsView: true
        }
    } as AppRouteRecordRaw,
]

export const asyncRouterMap: AppRouteRecordRaw[] = [{} as AppRouteRecordRaw, {} as AppRouteRecordRaw]

const router = createRouter({
    history: createWebHashHistory(),
    strict: true,
    routes: constantRouterMap,
    scrollBehavior: () => ({left: 0, top: 0})
} as RouterOptions)

export const resetRouter = (): void => {
    const resetWhiteNames = ['Redirect', 'Login', 'NoFind', 'Root']
    router.getRoutes().forEach((route) => {
        const {name} = route
        if (name && !resetWhiteNames.includes(name as string)) {
            router.hasRoute(name) && router.removeRoute(name)
        }
    })
}

export const setupRouter = (app: App<Element>) => {
    app.use(router)
}

export default router
