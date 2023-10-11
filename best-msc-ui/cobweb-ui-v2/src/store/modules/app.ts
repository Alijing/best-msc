import {ElMessage, ComponentSize} from "element-plus";
import {defineStore} from "pinia";
import {store} from "@/store";
import {useStorage} from "@/hooks/web/useStorage";
import {humpToUnderline, setCssVar} from "@/utils";

const {getStorage, setStorage} = useStorage();

interface AppState {
    // 面包屑
    breadcrumb: boolean
    // 面包屑图标
    breadcrumbIcon: boolean
    // 折叠菜单
    collapse: boolean
    // 是否只保持一个子菜单的展开
    uniqueOpened: boolean
    // 折叠图标
    hamburger: boolean
    // 全屏图标
    screenFull: boolean
    // 尺寸大小
    size: boolean
    // 多语言图标
    locale: boolean
    // 标签页
    tagsView: boolean
    // 标签页图标
    tagsViewIcon: boolean
    // logo
    logo: boolean
    // 固定 tool header
    fixedHeader: boolean
    // 是否开始灰色模式，用于特殊悼念日
    greyMode: boolean
    // 是否动态路由
    dynamicRouter: boolean
    // 路由跳转 loading
    pageLoading: boolean
    // layout 布局
    layout: LayoutType
    // 标题
    title: string
    // 登录信息存储字段-建议每个项目换一个字段，避免与其他项目冲突
    userInfo: string
    // 是否暗黑模式
    isDark: boolean
    // 组件尺寸
    currentSize: ComponentSize
    //
    sizeMap: ComponentSize[]
    // 是否移动端
    mobile: boolean
    // 显示页脚
    footer: boolean
    // 主题
    theme: ThemeTypes
    // 是否固定菜单
    fixedMenu: boolean
}


export const useAppStore = defineStore('app', {
    state: (): AppState => {
        return {
            userInfo: 'userInfo',
            sizeMap: ['default', 'large', 'small'],
            mobile: false,
            title: import.meta.env.VITE_APP_TITLE,
            pageLoading: false,

            breadcrumb: true,
            breadcrumbIcon: true,
            collapse: false,
            uniqueOpened: false,
            hamburger: true,
            screenFull: true,
            size: true,
            locale: true,
            tagsView: true,
            tagsViewIcon: true,
            logo: true,
            fixedHeader: true,
            footer: true,
            greyMode: false,
            dynamicRouter: getStorage('dynamicRouter') || false,
            fixedMenu: getStorage('fixedMenu') || false,

            layout: getStorage('layout') || 'classic',
            isDark: getStorage('isDark') || false,
            currentSize: getStorage('default') || 'default',
            theme: getStorage('theme') || {
                // 主题色
                elColorPrimary: '#409eff',
                // 左侧菜单边框颜色
                leftMenuBorderColor: 'inherit',
                // 左侧菜单背景颜色
                leftMenuBgColor: '#001529',
                // 左侧菜单浅色背景颜色
                leftMenuBgLightColor: '#0f2438',
                // 左侧菜单选中背景颜色
                leftMenuBgActiveColor: 'var(--el-color-primary)',
                // 左侧菜单收起选中背景颜色
                leftMenuCollapseBgActiveColor: 'var(--el-color-primary)',
                // 左侧菜单字体颜色
                leftMenuTextColor: '#bfcbd9',
                // 左侧菜单选中字体颜色
                leftMenuTextActiveColor: '#fff',
                // logo字体颜色
                logoTitleTextColor: '#fff',
                // logo边框颜色
                logoBorderColor: 'inherit',
                // 头部背景颜色
                topHeaderBgColor: '#fff',
                // 头部字体颜色
                topHeaderTextColor: 'inherit',
                // 头部悬停颜色
                topHeaderHoverColor: '#f6f6f6',
                // 头部边框颜色
                topToolBorderColor: '#eee'
            },
        } as AppState
    },
    getters: {
        getBreadcrumb(): boolean {
            return this.breadcrumb
        },
        getBreadcrumbIcon(): boolean {
            return this.breadcrumbIcon
        },
        getCollapse(): boolean {
            return this.collapse
        },
        getUniqueOpened(): boolean {
            return this.uniqueOpened
        },
        getHamburger(): boolean {
            return this.hamburger
        },
        getScreenfull(): boolean {
            return this.screenfull
        },
        getSize(): boolean {
            return this.size
        },
        getLocale(): boolean {
            return this.locale
        },
        getTagsView(): boolean {
            return this.tagsView
        },
        getTagsViewIcon(): boolean {
            return this.tagsViewIcon
        },
        getLogo(): boolean {
            return this.logo
        },
        getFixedHeader(): boolean {
            return this.fixedHeader
        },
        getGreyMode(): boolean {
            return this.greyMode
        },
        getDynamicRouter(): boolean {
            return this.dynamicRouter
        },
        getFixedMenu(): boolean {
            return this.fixedMenu
        },
        getPageLoading(): boolean {
            return this.pageLoading
        },
        getLayout(): LayoutType {
            return this.layout
        },
        getTitle(): string {
            return this.title
        },
        getUserInfo(): string {
            return this.userInfo
        },
        getIsDark(): boolean {
            return this.isDark
        },
        getCurrentSize(): ComponentSize {
            return this.currentSize
        },
        getSizeMap(): ComponentSize[] {
            return this.sizeMap
        },
        getMobile(): boolean {
            return this.mobile
        },
        getTheme(): ThemeTypes {
            return this.theme
        },
        getFooter(): boolean {
            return this.footer
        }
    },
    actions: {
        setBreadcrumb(breadcrumb: boolean) {
            this.breadcrumb = breadcrumb
        },
        setBreadcrumbIcon(breadcrumbIcon: boolean) {
            this.breadcrumbIcon = breadcrumbIcon
        },
        setCollapse(collapse: boolean) {
            this.collapse = collapse
        },
        setUniqueOpened(uniqueOpened: boolean) {
            this.uniqueOpened = uniqueOpened
        },
        setHamburger(hamburger: boolean) {
            this.hamburger = hamburger
        },
        setScreenfull(screenfull: boolean) {
            this.screenfull = screenfull
        },
        setSize(size: boolean) {
            this.size = size
        },
        setLocale(locale: boolean) {
            this.locale = locale
        },
        setTagsView(tagsView: boolean) {
            this.tagsView = tagsView
        },
        setTagsViewIcon(tagsViewIcon: boolean) {
            this.tagsViewIcon = tagsViewIcon
        },
        setLogo(logo: boolean) {
            this.logo = logo
        },
        setFixedHeader(fixedHeader: boolean) {
            this.fixedHeader = fixedHeader
        },
        setGreyMode(greyMode: boolean) {
            this.greyMode = greyMode
        },
        setDynamicRouter(dynamicRouter: boolean) {
            setStorage('dynamicRouter', dynamicRouter)
            this.dynamicRouter = dynamicRouter
        },
        setFixedMenu(fixedMenu: boolean) {
            setStorage('fixedMenu', fixedMenu)
            this.fixedMenu = fixedMenu
        },
        setPageLoading(pageLoading: boolean) {
            this.pageLoading = pageLoading
        },
        setLayout(layout: LayoutType) {
            if (this.mobile && layout !== 'classic') {
                ElMessage.warning('移动端模式下不支持切换其它布局')
                return
            }
            this.layout = layout
            setStorage('layout', this.layout)
        },
        setTitle(title: string) {
            this.title = title
        },
        setIsDark(isDark: boolean) {
            this.isDark = isDark
            if (this.isDark) {
                document.documentElement.classList.add('dark')
                document.documentElement.classList.remove('light')
            } else {
                document.documentElement.classList.add('light')
                document.documentElement.classList.remove('dark')
            }
            setStorage('isDark', this.isDark)
        },
        setCurrentSize(currentSize: ComponentSize) {
            this.currentSize = currentSize
            setStorage('currentSize', this.currentSize)
        },
        setMobile(mobile: boolean) {
            this.mobile = mobile
        },
        setTheme(theme: ThemeTypes) {
            this.theme = Object.assign(this.theme, theme)
            setStorage('theme', this.theme)
        },
        setCssVarTheme() {
            for (const key in this.theme) {
                setCssVar(`--${humpToUnderline(key)}`, this.theme[key])
            }
        },
        setFooter(footer: boolean) {
            this.footer = footer
        }
    }
})

export const useAppStoreWithOut = () => {
    return useAppStore(store)
}