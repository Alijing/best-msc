import {createRouter, createWebHashHistory} from 'vue-router'
import type {RouteRecordRaw, RouterOptions} from 'vue-router'
import type {App} from 'vue'
import {Layout, getParentLayout} from "@/utils/routerHelper";
import {useI18n} from "@/hooks/web/useI18n";

const {t} = useI18n();

export const constantRouterMap: AppRouteRecordRaw[] = [
    {
        path: '/',
        component: Layout,
        redirect: '/level/menu1/menu1-2',
        name: 'Root',
        meta: {
            hidden: true
        }
    } as AppRouteRecordRaw,
    {
        path: '/dashboard',
        component: Layout,
        redirect: '/dashboard/analysis',
        name: 'Dashboard',
        meta: {
            title: t('router.dashboard'),
            icon: 'ant-design:dashboard-filled',
            alwaysShow: true
        },
        children: [
            {
                path: 'analysis',
                component: () => import('@/views/dashboard/Analysis.vue'),
                name: 'Analysis',
                meta: {
                    title: t('router.analysis'),
                    noCache: true,
                    affix: true
                }
            },
            {
                path: 'workplace',
                component: () => import('@/views/dashboard/Workplace.vue'),
                name: 'Workplace',
                meta: {
                    title: t('router.workplace'),
                    noCache: true
                }
            }
        ]
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
            title: t('router.login'),
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

export const asyncRouterMap: AppRouteRecordRaw[] = [
    {
        path: '/level',
        component: Layout,
        redirect: '/level/menu1/menu1-1/menu1-1-1',
        name: 'Level',
        meta: {
            title: t('router.level'),
            icon: 'carbon:skill-level-advanced'
        },
        children: [
            {
                path: 'menu1',
                name: 'Menu1',
                component: getParentLayout(),
                redirect: '/level/menu1/menu1-1/menu1-1-1',
                meta: {
                    title: t('router.menu1')
                },
                children: [
                    {
                        path: 'menu1-1',
                        name: 'Menu11',
                        component: getParentLayout(),
                        redirect: '/level/menu1/menu1-1/menu1-1-1',
                        meta: {
                            title: t('router.menu11'),
                            alwaysShow: true
                        },
                        children: [
                            {
                                path: 'menu1-1-1',
                                name: 'Menu111',
                                component: () => import('@/views/demo/TheWelcome.vue'),
                                meta: {
                                    title: t('router.menu111')
                                }
                            }
                        ]
                    },
                    {
                        path: 'menu1-2',
                        name: 'Menu12',
                        component: () => import('@/views/demo/TheWelcome.vue'),
                        meta: {
                            title: t('router.menu12')
                        }
                    }
                ]
            },
            {
                path: 'menu2',
                name: 'Menu2',
                component: () => import('@/views/demo/TheWelcome.vue'),
                meta: {
                    title: t('router.menu2')
                }
            }
        ]
    } as AppRouteRecordRaw,
    {
        path: '/novel',
        component: Layout,
        redirect: '/level/menu1/menu1-1/menu1-1-1',
        name: 'Novel',
        meta: {
            title: '小说管理',
            icon: 'clarity:book-solid'
        },
        children: [
            {
                path: 'novel1',
                name: 'Novel1',
                component: () => import('@/views/demo/TheWelcome.vue'),
                meta: {
                    title: '小说列表'
                }
            },
            {
                path: 'novel2',
                name: 'Novel2',
                component: () => import('@/views/demo/TheWelcome.vue'),
                meta: {
                    title: '小说列表'
                }
            }
        ]
    } as AppRouteRecordRaw,
    {
        path: '/sys',
        component: Layout,
        redirect: '/level/menu1/menu1-1/menu1-1-1',
        name: 'System',
        meta: {
            title: '系统管理',
            icon: 'clarity:book-solid'
        },
        children: [
            {
                path: 'department',
                name: 'Department',
                component: () => import('@/views/sys/department/Department.vue'),
                meta: {
                    title: '部门管理'
                }
            },
            {
                path: 'novel2',
                name: 'Novel2',
                component: () => import('@/views/demo/TheWelcome.vue'),
                meta: {
                    title: '小说列表'
                }
            }
        ]
    } as AppRouteRecordRaw,
]

const router = createRouter({
    history: createWebHashHistory(),
    strict: true,
    routes: constantRouterMap as RouteRecordRaw[],
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
