package com.newland.jxh.store.common.util;


import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Properties;

/**
* @Author qyw
* @Description ftp 操作工具类
* @Date Created in 22:29 2018/8/12
* @Param 
* @Return 
**/
@Slf4j
public enum FtpUtilsEnum {
    INSTANCE;
    private static FtpUtilsEnum instance = null;
    private static FTPClient ftpClient = null;
    private String cache_dir = "e:/cache/";//用于压缩图片
    private static String server;
    private static int port;
    private static String userName;
    private static String userPassword ;
    private static String CONTROL_ENCODING;

       static {
           initConfig();
       }

    public static void initConfig(){
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("jxhStore.properties");
            server=properties.getProperty("ftp.server").trim();
            try {
                port=Integer.valueOf(properties.getProperty("ftp.port").trim());
            } catch (NumberFormatException e) {
                log.error("端口配置请使用阿拉伯数字!",e);
                e.printStackTrace();
            }
            userName=properties.getProperty("ftp.username").trim();
            userPassword=properties.getProperty("ftp.password").trim();
            CONTROL_ENCODING=properties.getProperty("ftp.controlEncoding","UTF-8").trim();
        } catch (IOException e) {
            log.error("读取ftp配置文件异常",e);
            e.printStackTrace();
        }

    }

    public static FtpUtilsEnum getInstance(){
        if(instance == null){
            instance = INSTANCE;
        }
        ftpClient = new FTPClient();
        return instance;
    }


    /**
     * 连接FTP服务器
     */
    private boolean connect(){
        boolean status = false;
        try {
            if(ftpClient.isConnected()){
                return true;
            }
            ftpClient.connect(server, port);
            status = true;
        } catch (SocketException e) {
            status = false;
            log.error("ftp连接异常",e);
            e.printStackTrace();
        } catch (IOException e) {
            status = false;
            log.error("ftp连接异常",e);
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 打开FTP服务器
     */
    public boolean open(){
        if(!connect()){
            return false;
        }
        boolean status = false;
        try {
            status = ftpClient.login(userName, userPassword);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                close();
                status = false;
                log.error("ftp应答异常,响应码:{}",reply);
            }
        } catch (IOException e) {
            log.error("ftp登录异常",e);
            e.printStackTrace();
            status = false;
        }
        return status;
    }


    /**
     * 关闭FTP服务器
     */
    public void close(){
        try {
            if(ftpClient != null){
                if(ftpClient.isConnected()){
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
                ftpClient = null;
            }
        } catch (IOException e) {
            log.error("ftp登出或断开连接异常",e);
            e.printStackTrace();
        }
    }


    /**
     * 上传文件到FTP服务器
     * @param filename 上传文件存储名称
     * @param path 上传文件存储路径,从根路径 "/" 开始
     * @param inputStream 上传文件输入流
     * @return
     */
    public boolean upload(String filename,String path,InputStream inputStream){
        boolean status = false;
        try {
            if(cd(path)){
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding(CONTROL_ENCODING);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                status = ftpClient.storeFile(filename, inputStream);
            }else{
                log.error("切换工作目录失败,目录:{}",path);
            }
        } catch (IOException e) {
            log.error("ftp上传文件异常",e);
        }finally {
            close();
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    /**
     * 上传文件到FTP服务器
     * @param filename 上传文件存储名称
     * @param path  上传文件存储路径,从根路径 "/" 开始
     * @param filepath  需要上传的文件路径
     * @return
     */
    public boolean upload(String filename,String path,String filepath){
        boolean status = false;
        InputStream inputStream=null;
        try {
            if(cd(path)){
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding(CONTROL_ENCODING);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                inputStream =new FileInputStream(new File(filepath));
                status = ftpClient.storeFile(filename, inputStream);
            }else{
                log.error("切换工作目录失败,目录:{}",path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    /**
     * 上传文件
     * @param filename 上传文件存储名称
     * @param path 上传文件存储路径,从根路径 "/" 开始
     * @param file 上传文件
     * @return
     */
    public boolean upload(String filename,String path,File file){
        boolean status = false;
        InputStream inputStream=null;
        try {
            if(cd(path)){
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding(CONTROL_ENCODING);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                inputStream = new FileInputStream(file);
                status = ftpClient.storeFile(filename,inputStream);
            }else{
                log.error("切换工作目录失败,目录:{}",path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    /**
     * 上传图片自动压缩处理
     * @param path
     * @param filename
     * @param input
     * @param quality
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public boolean uploadImage(String path,String filename,InputStream input,float quality,int maxWidth,int maxHeight){
        boolean status = false;
        FileInputStream input2=null;
        try {
            String suffex = filename.substring(filename.lastIndexOf(".")+1, filename.length());
            System.out.println(suffex);
            File imagePath = new File(cache_dir + filename);
            Thumbnails.of(input).outputQuality(quality).size(maxWidth,maxHeight).toFile(imagePath);
            input.close();

            if(!imagePath.exists()){
                return false;
            }

            cd(path);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(CONTROL_ENCODING);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            input2= new FileInputStream(imagePath);
            status = ftpClient.storeFile(filename,input2);
            imagePath.delete();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }finally {
            if(null != input2){
                try {
                    input2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }


    /**
     * 循环切换目录
     * @param dir
     * @return
     */
    public boolean cd(String dir){
        boolean status = true;
        try {
            String[] dirs = dir.split("/");
            if(log.isInfoEnabled()){
                log.info("切换路径:{}",dirs);
            }
            System.out.println(Arrays.toString(dirs));
            if(dirs.length == 0){
                return ftpClient.changeWorkingDirectory(dir);
            }
            status = ftpClient.changeToParentDirectory();
            for(String dirItem : dirs){
                status = status && ftpClient.changeWorkingDirectory(dirItem);
            }
        } catch (IOException e) {
            log.error("切换路径异常,路径:{},异常:{}",dir,e);
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /***
     * 创建目录
     * @param dir
     * @return
     */
    public boolean mkdir(String dir){
        boolean status = false;
        try {
            ftpClient.changeToParentDirectory();
            ftpClient.makeDirectory(dir);
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /***
     * 创建多个层级目录
     * @param dir dong/zzz/ddd/ewv
     * @return
     */
    public boolean mkdirs(String dir){
        String[] dirs = dir.split("/");
        if(dirs.length == 0){
            return false;
        }
        boolean status = false;
        try {
            ftpClient.changeToParentDirectory();
            for(String dirss : dirs){
                ftpClient.makeDirectory(dirss);
                ftpClient.changeWorkingDirectory(dirss);
            }

            ftpClient.changeToParentDirectory();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     * 删除文件夹
     * @param pathname
     * @return
     */
    public boolean rmdir(String pathname){
        try{
            return ftpClient.removeDirectory(pathname);
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 删除文件
     * @param pathname
     * @return
     */
    public boolean remove(String pathname){
        boolean status = false;
        try{
            status = ftpClient.deleteFile(pathname);
        }catch(Exception e){
            status = false;
        }
        return status;
    }

    /**
     * 移动文件或文件夹
     * @param pathname1
     * @param pathname2
     * @return
     */
    public boolean rename(String pathname1,String pathname2){
        try {
            return ftpClient.rename(pathname1, pathname2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
