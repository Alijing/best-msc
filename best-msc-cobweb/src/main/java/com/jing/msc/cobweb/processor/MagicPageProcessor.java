package com.jing.msc.cobweb.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.processor
 * @date : 2021/12/9 15:46
 * @description :
 */
public interface MagicPageProcessor {

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     * @param target page
     */
    public void process(Page page, Object target);

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    public Site getSite();

}
