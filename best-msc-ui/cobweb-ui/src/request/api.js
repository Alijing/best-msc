import {post, get, put, patch} from "./http";

export const novels = params => get('/novel/list', null);
export const novelInfoById = params => get('/novel/info/' + params, null);
export const novelUpdate = params => post('/novel/update', params);



