import axios from 'axios'
import {Message} from 'element-ui';
import apiConfig from '../../config/api.config'
import de from "element-ui/src/locale/lang/de";

axios.defaults.baseURL = apiConfig.baseURL;
// 请求超时 请求头
axios.defaults.timeout = 10000
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'; //'application/x-www-form-urlencoded';

//https://blog.csdn.net/lileLife/article/details/103770543?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_default&utm_relevant_index=2

// 请求拦截器
axios.interceptors.request.use(
  config => {
    // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加了
    // 即使本地存在token，也有可能token是过期的，所以在响应拦截器中要对返回状态进行判断
    const token = window.localStorage['token']; //token存储在浏览器
    token && (config.headers.Authorization = token);
    return config;
  },
  error => {
    return Promise.error(error);
  });


// 响应拦截器
axios.interceptors.response.use(
  response => {
    if (response.status === 200) {
      let code = response.data.code;
      switch (code) {
        case 200:
          return Promise.resolve(response);
        default:
          return Promise.reject(response.data);
      }
    } else {
      return Promise.reject(response);
    }
  },
  // 服务器状态码不是200的情况
  error => {
    if (error.response.status) {
      switch (error.response.status) {
        // 401: 未登录
        case 401:
          router.replace({
            path: '/login',
            query: {redirect: router.currentRoute.fullPath}
          });
          break;
        case 403:
          Message({
            type: 'error',
            message: '登录过期，请重新登录',
            offset: 60,
            duration: 1000
          });
          // 清除token
          localStorage.removeItem('token');
          store.commit('loginSuccess', null);
          setTimeout(() => {
            router.replace({
              path: '/login',// 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
              query: {
                redirect: router.currentRoute.fullPath
              }
            });
          }, 1000);
          break;
        // 404请求不存在
        case 404:
          Message({
            type: 'error',
            message: '地址不存在',
            offset: 60,
            duration: 1000
          });
          break;
        // 其他错误，直接抛出错误提示
        default:
          Message({
            type: 'error',
            message: error.response.data.msg,
            offset: 60,
            duration: 1000
          });
      }
      return Promise.reject(error.response);
    }
  }
);

/**
 * Get请求
 * @param url 请求地址
 * @param params 参数
 * @returns {Promise<unknown>}
 */
export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    })
      .then(res => {
        resolve(res.data)
      })
      .catch(error => {
        reject(error)
      })
  })
}

/**
 * Post请求
 * @param url 请求地址
 * @param params 参数
 * @returns {Promise<unknown>}
 */
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(res => {
        resolve(res.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * 封装 patch 请求
 * @param url
 * @param params
 */
export function patch(url, params) {
  return new Promise((resolve, reject) => {
    axios.patch(url, params)
      .then(resp => {
        resolve(resp.data);
      }, err => {
        reject(err);
      })
  })
}

/**
 * put 请求
 * @param  url
 * @param  params
 */
export function put(url, params) {
  return new Promise((resolve, reject) => {
    axios.put(url, params)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/*
 *  文件上传
 *  url:请求地址
 *  params:参数
 * */
export function fileUpload(url, params) {
  return new Promise((resolve, reject) => {
    axios({
      url: url,
      method: 'post',
      data: params,
      headers: {'Content-Type': 'multipart/form-data'}
    }).then(res => {
      resolve(res)
    })
      .catch(error => {
        reject(error)
      })
  })
}


