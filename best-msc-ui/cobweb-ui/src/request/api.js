import {post, get, put, patch, download} from "./http";

export const novels = params => post('/novel/list', params);
export const novelInfoById = params => get('/novel/info/' + params, null);
export const novelUpdate = params => post('/novel/update', params);

export const crawlingChapter = params => get('/novel/crawling/chapter/' + params, params);
export const changeChapterName = params => get('/novel/change/chapter/name/' + params, params);
export const crawlingChapterContent = params => get('/novel/crawling/content/' + params, params);




