package com.hxzm.service.impl;

import com.hxzm.dao.common.datasource.cmsDS;
import com.hxzm.dao.common.datasource.payDS;
import com.hxzm.dao.common.datasource.siteDS;
import com.hxzm.dao.mapper.WelcomeMapper;
import com.hxzm.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    @Autowired
    WelcomeMapper welcomeMapper;

    @Override
    @payDS
    public void service1() {
        System.out.println(welcomeMapper.find1());
    }

    @Override
    @cmsDS
    public void service2() {
        System.out.println(welcomeMapper.find1());
    }

    @Override
    @siteDS
    public void service3() {
        System.out.println(welcomeMapper.find1());
    }

    @Override
    @siteDS
    public void service4() {
        System.out.println(welcomeMapper.find2());
    }
}
