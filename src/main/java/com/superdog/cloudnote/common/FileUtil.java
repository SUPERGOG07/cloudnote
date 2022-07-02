package com.superdog.cloudnote.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class FileUtil {

    /**
     * 获得项目临时文件夹位置路径
     * @return
     * @throws IOException
     */
    public static String getFileBasePath() throws IOException {
        File file = new File("");

        String path = file.getCanonicalPath() + "\\src\\main\\resources\\templates";

        File dir = new File(path);
        if(!dir.exists()) dir.mkdir();

        return path;
    }

    /**
     * 将上传的文件存入临时文件夹里，然后用uuid随机命名文件
     * 返回操作结果map，如果成功存入，则返回成功状态和文件名，操作失败则返回失败状态和失败原因
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String,String > uploadFile(MultipartFile file) throws IOException {
        Map<String,String> result = new HashMap<>();
        String basePath = null;
        try {
            basePath = getFileBasePath();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件路径出错");
            result.put("state","false");
            result.put("msg","获取文件路径出错");
        }

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newName = UUID.randomUUID().toString() + suffix;

        try {
            file.transferTo(new File(basePath + "\\" + newName));

            result.put("state","success");
            result.put("msg",newName);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("state","false");
            result.put("msg","上传失败");
            deleteFile(newName);
        }

        return result;
    }

    /**
     * 删除临时文件夹下的特定名称文件
     * @param fileName
     * @throws IOException
     */
    public static void deleteFile(String fileName) throws IOException{
        File file = new File(new File("").getCanonicalPath()
                +"\\src\\main\\resources\\templates\\"+fileName);
        if(!file.exists()){
            log.info("文件不存在-->"+fileName);
        }else {
            file.delete();
            log.info("删除成功-->"+fileName);
        }
    }

    public static void downloadFile(String name, HttpServletResponse response){
        try{
            String basePath = getFileBasePath();

            FileInputStream inputStream = new FileInputStream(new File(basePath + "\\" + name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
