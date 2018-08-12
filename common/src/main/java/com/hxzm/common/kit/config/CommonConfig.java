package com.hxzm.common.kit.config;

import javapns.Push;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class CommonConfig {

    /*
     *  // endpoint以杭州为例，其它region请按实际情况填写
        String endPoint = "http://oss-cn-shanghai.aliyuncs.com";
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        String accessKeyId = "a4OiSjh7d575rc3e";
        String accessKeySecret = "iIGUmWQUvrv4oBxaTZ9gkhUzV79PNN";
        final String    bucket = "xmyd-ops-0001";
     * */
    @Value("${aliyun.endPoint}")
    private String endPoint;
    
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    
    @Value("${aliyun.bucket}")
    private String bucket;
    
    @Value("${aliyun.imagurl}")
    private String imageurl;
    
    @Value("${jiguang.masterKey}")
    private String masterKey;
    
    @Value("${jiguang.appKey}")
    private String appKey;
    
    @Value("${jiguang.isProd}")
    private boolean isProd;
    

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setVersion(1.0)
                .disableInnerClassSerialization()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setDateFormat("yyyy-MM-dd")
                .create();
    }
    
    @Bean
    public OSSClient ossClient() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }
    
//    @Bean
//    public Push push() {
//
//        Push.
//        return new Push(masterKey, appKey, isProd);
//    }
    
}
