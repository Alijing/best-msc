import {post, get, put, patch, download} from "./http";

export const novels = params => post('/novel/list', params);
export const novelInfoById = params => get('/novel/info/' + params, null);
export const novelBatchDelete = params => post('/novel/batch/delete', params);


export const crawlConfigByNovelId = params => get('/novel/crawl/config/' + params, null);
export const crawlConfigUpdate = params => post('/novel/crawl/config/update', params);

export const copyNovel = params => get('/novel/info/copy/' + params, null);
export const crawlingChapter = params => get('/novel/crawling/chapter/' + params, params);
export const changeChapterName = params => get('/novel/change/chapter/name/' + params, params);
export const crawlingChapterContent = params => get('/novel/crawling/content/' + params, params);




