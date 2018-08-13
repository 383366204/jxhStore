package com.newland.jxh.store;

import com.newland.jxh.store.common.util.FtpUtilsEnum;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author qyw
 * @Description TODO
 * @Date Created in 16:17 2018/8/13
 */
public class FtpUtilTest {

    InputStream inputStream=null;
    FtpUtilsEnum instance=null;

    @Before
    public void init(){
        ClassPathResource classPathResource = new ClassPathResource("testFtp.html");
        try {
            inputStream= classPathResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = FtpUtilsEnum.getInstance();
    }

    @Test
    public void testLogin() throws Exception {
        System.out.println(FtpUtilsEnum.getInstance().open());
    }


    @Test
    public void testUpload() throws Exception {
        if(instance.open()){
            boolean upload = instance.upload("testFtp.html", "root/dmweb/rwdwebapp3.0/bi_web.war/ngreward3_0/", inputStream);
            System.out.println(upload==true?"上传成功":"上传失败");
        }
    }

}
