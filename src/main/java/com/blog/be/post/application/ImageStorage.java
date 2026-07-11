package com.blog.be.post.application;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStorage {
    String upload(MultipartFile multipartFile, String uuid);

    void deleteMany(List<String> deleteList);

    void deleteOne(String key);
}

