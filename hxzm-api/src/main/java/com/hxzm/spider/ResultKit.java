package com.hxzm.spider;

import com.hxzm.dao.domain.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

/**
 * @author zhangyaohai
 * @create 2018-08-25 17:10
 **/
public class ResultKit {


    public static Article getResult(String wechartArticleHtml){
        Document wechartArticlDoc = Jsoup.parse(wechartArticleHtml);
        Elements tittle = wechartArticlDoc.select(".rich_media_title");
        Element author = wechartArticlDoc.select(".rich_media_meta.rich_media_meta_text").get(1);
        Elements time = wechartArticlDoc.select(".rich_media_meta.rich_media_meta_text#post-date");
        Elements content = wechartArticlDoc.select(".rich_media_content#js_content");
        String alterContent = content.html().replace("data-src", "src");// 将属性data-src替换为src，否则图片不能正常显示
        Elements publicSign = wechartArticlDoc.select(".rich_media_meta.rich_media_meta_text.rich_media_meta_nickname");

        Article article = new Article();
        article.setContent(alterContent);

        // article.setKeywords();
        article.setTitle(tittle.text());
        article.setTopicId(1);
        article.setUserId(1);
        article.setCreateTime(new Date());
        article.setStatus(0);

        return article;
    }
}
