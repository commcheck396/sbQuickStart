package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.utils.OSSUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String URL = OSSUtil.uploadFile(fileName, file.getInputStream());
        return Result.success(URL);
    }
}
