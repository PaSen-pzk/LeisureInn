package com.sli.web.controller;

import com.sli.common.result.ResultModel;
import com.sli.io.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * minio文件上传下载控制器
 */
@RestController
@RequestMapping("/api/minio/file")
public class MinioFileController {

    private final MinioUtil minioUtil;
    public MinioFileController(@Autowired MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }
    @PostMapping(name = "/upload")
    public ResultModel upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        minioUtil.upload(file, fileName);
        return ResultModel.success("上传成功");
    }

    @GetMapping("download")
    public ResultModel download(String fileName) throws Exception {
        return ResultModel.success(minioUtil.download(fileName));
    }

    @GetMapping("review")
    public ResultModel review(String fileName) {
        return ResultModel.success(minioUtil.getFileUrl(fileName));
    }
}
