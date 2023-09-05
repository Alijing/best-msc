import {post, get, put, patch, download, download1} from "./http";

export const novels = params => post('/novel/list', params);
export const novelInfoById = params => get('/novel/info/' + params, null);
export const novelBatchDelete = params => post('/novel/batch/delete', params);


export const crawlConfigByNovelId = params => get('/novel/crawl/config/' + params, null);
export const crawlConfigUpdate = params => post('/novel/crawl/config/update', params);

export const copyNovel = params => get('/novel/crawl/config/copy/' + params, null);
export const crawlingChapter = params => get('/novel/crawl/chapter/' + params, null);
export const changeChapterName = params => get('/novel/change/chapter/name/' + params, params);
export const crawlingChapterContent = params => get('/novel/crawl/content/' + params, params);


export const deliveryOrderMerge = (params, callback, timeout) => download('/binbin/deliveryOrder/merge', params, callback, timeout);

