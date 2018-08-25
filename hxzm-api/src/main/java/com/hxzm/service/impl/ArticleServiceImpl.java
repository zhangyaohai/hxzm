package com.hxzm.service.impl;

import com.hxzm.api.ArticleRequest;
import com.hxzm.dao.mapper.ArticleMapper;
import com.hxzm.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangyaohai
 * @create 2018-08-25 18:18
 **/

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public void save(ArticleRequest articleRequest) {

    }
}
