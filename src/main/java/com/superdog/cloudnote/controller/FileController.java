package com.superdog.cloudnote.controller;

import com.superdog.cloudnote.common.FileUtil;
import com.superdog.cloudnote.common.HttpUtil;
import com.superdog.cloudnote.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;



@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public R<String> upload(@RequestParam(value = "uploadFile",required = false) MultipartFile file) throws IOException {
        Map<String,String > result1 = FileUtil.uploadFile(file);
        if(result1.get("state").equals("false")){
            return R.error(result1.get("msg"));
        }else {
            Map<String,String> result2 = HttpUtil.uploadFIleToSM(result1.get("msg"));
            if(result2.get("state").equals("false")){
                return R.error(result2.get("msg"));
            }else {
                return R.success(result2.get("msg"));
            }
        }
    }
}
