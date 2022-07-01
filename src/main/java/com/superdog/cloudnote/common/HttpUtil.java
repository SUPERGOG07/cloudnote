package com.superdog.cloudnote.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class HttpUtil {
    public static boolean uploadFIleToSM(String fileName){
        boolean flag = true;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try{
            File file = new File(new File("").getCanonicalPath()
                    +"\\src\\main\\resources\\templates\\"+fileName);

            HttpPost httpPost = new HttpPost("https://sm.ms/api/v2/upload");

            httpPost.addHeader("Authorization","IIPSQ7U2r8vsYeAtdZ33hwVV3ffobzIT");
            httpPost.addHeader("accept","*/*");
            httpPost.addHeader("connection","Keep-Alive");
            httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.66 Safari/537.36 Edg/103.0.1264.44");

            FileBody fileBody = new FileBody(file);
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("smfile",fileBody).build();
            httpPost.setEntity(reqEntity);
            log.info("executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                log.info("----------------------------------------");
                log.info(response.getStatusLine().toString());
                if(response.getStatusLine().getStatusCode()!=200){
                    flag = false;
                }
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    log.info(EntityUtils.toString(resEntity, Charset.defaultCharset()));
                }
                EntityUtils.consume(resEntity);
            }  finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

}
