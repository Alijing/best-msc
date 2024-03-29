import {
    AxiosConfig,
    AxiosError,
    AxiosRequestHeaders,
    AxiosResponse,
    InternalAxiosRequestConfig
} from "./types"
import {ElMessage} from "element-plus"
import qs from 'qs'
import router, {resetRouter} from '@/router'
import {useStorage} from "@/hooks/web/useStorage";


const {clear} = useStorage()

const config: AxiosConfig = {
    /**
     * api 请求基础路径
     */
    baseUrl: {
        // 开发环境接口前缀
        base: 'api',
        // 打包开发环境接口前缀
        dev: 'api',
        // 打包生产环境接口前缀
        pro: 'api',
        // 打包测试环境接口前缀
        test: 'api'
    },
    /**
     * 接口成功返回状态码
     */
    code: 20000,
    /**
     * 接口请求超时时间
     */
    timeout: 60000,
    /**
     * 默认接口请求类型
     * 可选值：application/x-www-form-urlencoded multipart/form-data
     */
    defaultHeaders: 'application/json',
    interceptors: {}
} as AxiosConfig

const defaultRequestInterceptors = (config: InternalAxiosRequestConfig) => {
    if ('post' === config.method &&
        'application/x-www-form-urlencoded' === (config.headers as AxiosRequestHeaders)["Content-Type"]) {
        config.data = qs.stringify(config.data)
    }
    if ('get' === config.method && config.params) {
        let url = config.url as string;
        url += '?'
        const keys = Object.keys(config.params);
        for (const key of keys) {
            if (config.params[key] != void 0 && config.params[key] !== null) {
                url += `${key}=${encodeURIComponent(config.params[key])}`
            }
        }
        url = url.substring(0, url.length - 1)
        config.params = {}
        config.url = url
    }
    return config
};
(error: AxiosError) => {
    console.log(error)
    Promise.reject(error)
}

const defaultResponseInterceptors = (response: AxiosResponse<any>) => {
    if ('blob' === response?.config?.responseType) {
        // 如果是文件流，直接过
        return response
    }
    switch (response.code) {
        case config.code:
            return response;
        case 401:
            ElMessage.error(response.message)
            clear()
            resetRouter() // 重置静态路由表
            router.push({
                name: "Login",
                query: {
                    redirect: router.currentRoute.value.fullPath // router对象自带的属性
                }
            })
            break;
        case 403:
            ElMessage.error(response.message)
            router.push({name: 'Analysis'})
            break;
        default:
            ElMessage.error(response.message)
            break;
    }
};
(error: AxiosError) => {
    console.log(error)
    ElMessage.error(response.data.message)
    return Promise.reject(error)
}

export {defaultRequestInterceptors, defaultResponseInterceptors}
export default config