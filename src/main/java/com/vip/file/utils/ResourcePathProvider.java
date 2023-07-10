package com.vip.file.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zkc
 * @description
 * @Date 2023/6/9 11:13 星期五
 * @Version 1.0
 */
@Component
public class ResourcePathProvider {
    private final ResourceLoader resourceLoader;

    @Autowired
    public ResourcePathProvider(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getResourcePath(String fileName) {
        try {
            // 使用ResourceLoader加载资源文件
            Resource resource = resourceLoader.getResource("classpath:" + fileName);
            // 获取资源文件的路径
            String filePath = resource.getFile().getAbsolutePath();
            return filePath;
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return null;
        }
    }
}
