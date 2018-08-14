package com.newland.jxh.store.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @Author qyw
 * @Description TODO
 * @Date Created in 22:57 2018/8/14
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {
    private  String ImgCacheDir;//压缩图片本地缓存文件夹

    @NotNull
    private  String server;

    private  int port;
    @NotBlank
    private  String username;
    @NotNull
    private  String password;
    private  String controlEncoding;
}
