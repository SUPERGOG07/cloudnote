package com.superdog.cloudnote.common;

import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtil {

    /**
     * 上传临时文件夹的特定文件到SM图库里
     * 返回结果：成功返回成功状态以及文件url，失败则返回失败状态以及失败信息
     * @param fileName
     * @return
     */
    public static Map<String,String> uploadFIleToSM(String fileName){
        Map<String ,String> result = new HashMap<>();

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
                    result.put("state","false");
                    result.put("msg","接入图床失败");
                }
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String entityJson = EntityUtils.toString(resEntity, Charset.defaultCharset());
                    log.info(entityJson);
                    String url = JSONObject.parseObject(entityJson)
                            .getObject("data", JSONObject.class).getString("url");
                    log.info(url);
                    result.put("state","success");
                    result.put("msg",url);
                }

                EntityUtils.consume(resEntity);
            }  finally {
                log.info("----------------------------------------");
                FileUtil.deleteFile(fileName);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result.put("state","false");
            result.put("msg","接入图床失败");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("state","false");
            result.put("msg","接入图床失败");
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
