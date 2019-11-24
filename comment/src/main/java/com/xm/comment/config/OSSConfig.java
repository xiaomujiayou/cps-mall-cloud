package com.xm.comment.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class OSSConfig {

    @Value("${oss.ali.endpoint}")
    private String endpoint;
    @Value("${oss.ali.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.ali.bucketName}")
    private String bucketName;
    @Value("${spring.profiles.active}")
    private String profile;

    @Bean("ossClient")
    public OSS ossClient(){
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
