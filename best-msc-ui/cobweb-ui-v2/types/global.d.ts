declare module 'qs'

declare global {

    type Nullable<T> = T | null;

    type ElRef<T extends HTMLElement = HTMLDivElement> = Nullable<T>

    type Recordable<T = any, K = string> = Record<K extends null | undefined ? string : K, T>

    type ComponentRef<T> = InstanceType<T>

    type LocaleType = 'zh-CN' | 'en'

    type LayoutType = 'classic' | 'topLeft' | 'top' | 'cutMenu'

    type AxiosHeaders =
        | 'application/json'
        | 'application/x-www-form-urlencoded'
        | 'multipart/form-data'

    type AxiosMethod = 'get' | 'post' | 'delete' | 'put'

    type AxiosResponseType = 'arraybuffer' | 'blob' | 'document' | 'json' | 'text' | 'stream'

    interface AxiosConfig {
        params?: any
        data?: any
        url?: string
        method?: AxiosMethod
        headersType?: string
        responseType?: AxiosResponseType
    }

    interface IResponse<T = any> {
        code: number
        data: T extends any ? T : T & any
    }

    interface ThemeTypes {
        elColorPrimary?: string
        leftMenuBorderColor?: string
        leftMenuBgColor?: string
        leftMenuBgLightColor?: string
        leftMenuBgActiveColor?: string
        leftMenuCollapseBgActiveColor?: string
        leftMenuTextColor?: string
        leftMenuTextActiveColor?: string
        logoTitleTextColor?: string
        logoBorderColor?: string
        topHeaderBgColor?: string
        topHeaderTextColor?: string
        topHeaderHoverColor?: string
        topToolBorderColor?: string
    }

}