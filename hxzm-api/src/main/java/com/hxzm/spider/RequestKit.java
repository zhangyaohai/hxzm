package com.hxzm.spider;

import com.hxzm.dao.domain.Article;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyaohai
 * @create 2018-08-25 17:10
 **/
public class RequestKit {

    public static void main(String[] args) {


    }

    public static Article get(String urlStr){
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlStr);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);

            try {
                StatusLine statusLine = response1.getStatusLine();
                System.out.println(statusLine.getReasonPhrase());

                // System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();

                String result = EntityUtils.toString(entity1, "UTF-8");

                Article  con = ResultKit.getResult(result);

                System.out.println(con.getContent());  System.out.println(con.getTitle());

                EntityUtils.consume(entity1);

                return con;

            } finally {
                response1.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    private static void post(){

        CloseableHttpResponse response2 = null;
        try {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://targethost/login");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
         response2 = httpclient.execute(httpPost);
        System.out.println(response2.getStatusLine());
        HttpEntity entity2 = response2.getEntity();
        EntityUtils.consume(entity2);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response2.close();
            }catch (Exception e){
               e.printStackTrace();
            }

        }
    }

}
