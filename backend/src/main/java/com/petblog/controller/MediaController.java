package com.petblog.controller;

import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    private static final String[] IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"};
    private static final String[] VIDEO_TYPES = {"video/mp4", "video/webm", "video/ogg", "video/quicktime"};
    private static final long IMAGE_MAX_SIZE = 10 * 1024 * 1024;
    private static final long VIDEO_MAX_SIZE = 50 * 1024 * 1024;

    @PostMapping("/upload/image")
    public Result uploadImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return uploadFile(request, file, "image", IMAGE_TYPES, IMAGE_MAX_SIZE);
    }

    @PostMapping("/upload/video")
    public Result uploadVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return uploadFile(request, file, "video", VIDEO_TYPES, VIDEO_MAX_SIZE);
    }

    private Result uploadFile(HttpServletRequest request, MultipartFile file,
                              String typeDir, String[] allowedTypes, long maxSize) {
        if (file.isEmpty()) {
            return Result.error("请选择文件");
        }

        String contentType = file.getContentType();
        boolean typeAllowed = false;
        for (String type : allowedTypes) {
            if (type.equals(contentType)) {
                typeAllowed = true;
                break;
            }
        }
        if (!typeAllowed) {
            return Result.error("不支持的文件类型: " + contentType);
        }

        if (file.getSize() > maxSize) {
            String maxDesc = typeDir.equals("image") ? "10MB" : "50MB";
            return Result.error("文件大小不能超过 " + maxDesc);
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        String relativePath = typeDir + "/" + fileName;

        File baseDir = new File(uploadPath);
        if (!baseDir.isAbsolute()) {
            baseDir = new File(System.getProperty("user.dir"), uploadPath);
        }

        File destFile = new File(baseDir, relativePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }

        String url = "/uploads/" + relativePath;
        return Result.success("上传成功", url);
    }
}
