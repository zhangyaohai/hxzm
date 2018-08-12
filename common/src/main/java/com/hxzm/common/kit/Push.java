package com.hxzm.common.kit;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import com.google.gson.Gson;
import com.hxzm.common.kit.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import java.util.Map;

public class Push {
    
    Logger logger = Logger.getLogger(Push.class);
    
    // private String masterKey; //344bb8c07273d44194333a3b
    // private String appKey; //6e80db3fbb2828c091e65e17

    private JPushClient jpush = null;

    private static final Gson GSON = new Gson();

    public Push(String masterKey, String appKey, boolean isProd) {
        ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(5);
        config.setConnectionTimeout(10 * 1000); // 10 seconds
        config.setSSLVersion("TLSv1.1"); // JPush server supports SSLv3, TLSv1, TLSv1.1, TLSv1.2
        config.setApnsProduction(isProd);
        jpush = new JPushClient(masterKey, appKey, null, config);
    }

    public int pushData(String title, String altMsg, Map<String, String> data, String registerId) {
        return pushData(title, altMsg, GSON.toJson(data), data, registerId, Constants.SOURCE_TYPE_UNKNOW);
    }

    public int pushData(String title,String altMsg, String content,Map<String,String> extraData, String registerId, int sourceType) {
        try {
            //IOS和安卓一起  
            if(StringUtils.isBlank(registerId)){
                logger.info("没有 push registerId, 取消推送");
                return 0;
            }

            String[] registerInfo = registerId.split(Constants.STR_SPLIT);
            registerId = registerInfo[0];
            if (registerInfo.length > 1 && sourceType == Constants.SOURCE_TYPE_UNKNOW) {
                sourceType = NumberUtils.toInt(registerInfo[1]);
            }

            logger.info("push 信息registerId："+registerId +"  内容："+content+"  sourceType："+sourceType);
            PushResult pr = null;
            switch(sourceType){
                case Constants.SOURCE_TYPE_Android:
                    pr = jpush.sendAndroidMessageWithRegistrationID(title, content, registerId);
                    jpush.sendAndroidNotificationWithRegistrationID(title, altMsg,  extraData, registerId);
                    break;
                case Constants.SOURCE_TYPE_IOS:
                    pr = jpush.sendIosMessageWithRegistrationID(title, content, registerId);
                    jpush.sendIosNotificationWithRegistrationID(altMsg,  extraData, registerId);
                    break;
                 default: return 0;
            }
            logger.info(pr == null ? null:"push 结果："+pr.isResultOK()+pr.getResponseCode());
        } catch (APIConnectionException e) {
            
        } catch (APIRequestException e) {
            
        }
        return sourceType;
    }

    public enum Type {
        FREE_DEPOSIT_SUCCESS(100, "免押金成功", "优拜免押金通知"),
        FREE_DEPOSIT_FAIL(101, "免押金失败", "优拜免押金通知");

        public final int code;
        public final String message;
        public final String title;
        Type(int code, String message, String title) {
            this.code = code;
            this.message = message;
            this.title = title;
        }
    }

}
