import config, {defaultRequestInterceptors, defaultResponseInterceptors} from './config'
import axios, {CreateAxiosDefaults} from "axios";
import {RequestConfig, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from "./types";

const {interceptors, baseUrl} = config;

export const PATH_URL = baseUrl[import.meta.env.VITE_API_BASE_PATH];

const {requestInterceptors, responseInterceptors} = interceptors;

const abortControllerMap: Map<string, AbortController> = new Map<string, AbortController>()

const axiosInstance: AxiosInstance = axios.create({
    ...config,
    baseURL: PATH_URL
} as CreateAxiosDefaults)

axiosInstance.interceptors.request.use((res: InternalAxiosRequestConfig) => {
    const controller = new AbortController();
    const url = res.url || '';
    res.signal = controller.signal;
    abortControllerMap.set(url, controller)
    return res
})

axiosInstance.interceptors.response.use((res: AxiosResponse) => {
    const url = res.config.url || '';
    abortControllerMap.delete(url)
    return res.data
}, (err: any) => err)

axiosInstance.interceptors.request.use(requestInterceptors || defaultRequestInterceptors)
axiosInstance.interceptors.response.use(responseInterceptors || defaultResponseInterceptors)

const service = {
    request: (config: RequestConfig) => {
        return new Promise((resolve, reject) => {
            if (config.interceptors?.requestInterceptors) {
                config = config.interceptors.requestInterceptors(config as any)
            }
            console.log('-- ' + config)
            axiosInstance.request(config)
                .then((res) => {
                    resolve(res)
                })
                .catch((err: any) => {
                    reject(err)
                })
        })
    },
    cancelRequest: (url: string | string[]) => {
        const urls = Array.isArray(url) ? url : [url];
        for (const _url of urls) {
            abortControllerMap.get(_url)?.abort()
            abortControllerMap.delete(_url)
        }
    },
    cancelAllRequest() {
        for (const [_, controller] of abortControllerMap) {
            controller.abort()
        }
        abortControllerMap.clear()
    }
}
export default service