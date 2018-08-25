package com.hxzm.api;

import lombok.Data;

/**
 * @author zhangyaohai
 * @create 2018-08-25 18:21
 **/
@Data
public class ArticleRequest {

    private String title;

    private String content;

    private Integer topicId;

    private String tag;

    private String keywords;
}
