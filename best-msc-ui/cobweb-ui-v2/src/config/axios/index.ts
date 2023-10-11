import config from "./config";
import service from "./service";
import {RequestConfig} from "./types";

const {defaultHeaders} = config;


const request = (options: AxiosConfig) => {
    const {url, method, params, data, headersType, responseType} = options
    console.log('-- ' + url)
    return service.request({
        url: url,
        method,
        params,
        data,
        responseType: responseType,
        headers: {
            'Content-Type': headersType || defaultHeaders
        }
    } as RequestConfig)
}

export default {
    get: <T = any>(options: AxiosConfig) => {
        return request({method: 'get', ...options}) as Promise<IResponse<T>>;
    },
    post: <T = any>(options: AxiosConfig) => {
        return request({method: 'post', ...options}) as Promise<IResponse<T>>;
    },
    delete: <T = any>(options: AxiosConfig) => {
        return request({method: 'delete', ...options}) as Promise<IResponse<T>>;
    },
    put: <T = any>(options: AxiosConfig) => {
        return request({method: 'put', ...options}) as Promise<IResponse<T>>;
    },
    cancelRequest: (url: string | string[]) => {
        return service.cancelRequest(url)
    },
    cancelAllRequest: () => {
        return service.cancelAllRequest()
    },
}