package com.hxzm.controller;

import com.hxzm.api.ArticleRequest;
import com.hxzm.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyaohai
 * @create 2018-08-25 18:16
 **/

@RestController
@RequestMapping(value = "/hxzm/article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @PostMapping
    public void save(@RequestBody ArticleRequest articleRequest){
        articleService.save(articleRequest);
    }

}
