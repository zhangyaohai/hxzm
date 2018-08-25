package com.hxzm.spider;

import com.hxzm.dao.domain.Article;

/**
 * @author zhangyaohai
 * @create 2018-08-25 18:10
 **/
public class Spider {

    // https://mp.weixin.qq.com/s/4jOWd8KuTbLeqnLoLm-GwQ
    public static Article weixinGetArticle(String url){
        return RequestKit.get(url);
    }

}
