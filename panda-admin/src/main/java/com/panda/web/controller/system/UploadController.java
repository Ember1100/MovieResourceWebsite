package com.panda.web.controller.system;

import com.panda.system.service.SysCinemaService;
import com.panda.web.controller.BaseController;
import com.panda.web.param.UploadResponse;
import com.panda.web.utils.MinioUtil;
import com.panda.web.utils.ResultData;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class UploadController extends BaseController {

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("/upload/images")
    public ResultData uploadUser(@RequestParam(value = "file") MultipartFile file) {
        UploadResponse response = null;
        try {
            response =  minioUtil.uploadFile(file,"images");
        } catch (Exception e) {
            log.error("上传失败",e);
        }
        assert response != null;
        return ResultData.ok(response.getMinIoUrl());
    }

    /**
     * 上传视频
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadVideo")
    public ResultData uploadVideo(@RequestParam(value = "file") MultipartFile file) {
        UploadResponse response = null;
        try {
            response = minioUtil.uploadVideo(file, "video");
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        assert response != null;
        return ResultData.ok(response);
    }


}
