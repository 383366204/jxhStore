package com.newland.jxh.store.common.util;


import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
*{@link FTPClient,Thumbnails}
* @Author qyw
* @Description ftp 工具:上传,下载,移动,删除,创建,上传并压缩图片
* @Date Created in 19:49 2018/8/13
* @Param 
* @Return 
**/
@Slf4j
public enum FtpUtilsEnum {
    INSTANCE;
    private static FtpUtilsEnum instance = null;
    private static FTPClient ftpClient = null;
    private static String ImgCacheDir;//压缩图片本地缓存文件夹
    private static String server;
    private static int port;
    private static String userName;
    private static String userPassword ;
    private static String controlEncoding;

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
            controlEncoding=properties.getProperty("ftp.controlEncoding","UTF-8").trim();
            ImgCacheDir=properties.getProperty("ftp.ImgCacheDir").trim();
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
        boolean status;
        try {
            if(ftpClient.isConnected()){
                return true;
            }
            ftpClient.connect(server, port);
            status = true;
        } catch (SocketException e) {
            System.out.println(ftpClient.getReplyString());
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
        boolean status;
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
     * @param path 上传文件存储路径,为空或空字符表示上传到ftp主目录
     * @param inputStream 上传文件输入流
     * @return
     */
    public boolean upload(String filename,String path,InputStream inputStream){
        checkNotNull(filename,"上传文件存储名称不能为空");
        checkNotNull(path,"上传文件输入流不能为空");
        boolean status = false;
        if (cd(path)) {
            try {
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding(controlEncoding);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                status = ftpClient.storeFile(filename, inputStream);
                //System.out.println(ftpClient.getReplyString());
            } catch (IOException e) {
                log.error("ftp上传文件异常,ReplyString:{}",ftpClient.getReplyString(),e);
                e.printStackTrace();
            } finally {
                close();
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            log.error("切换工作目录失败,目录:{}", path);
            return false;
        }
        return status;
    }

    /**
     * 上传文件到FTP服务器
     * @param filename 上传文件存储名称
     * @param path  上传文件存储路径,为空或空字符表示上传到ftp主目录
     * @param filepath  需要上传的文件路径
     * @return
     */
    public boolean upload(String filename,String path,String filepath){
        checkNotNull(filename,"上传文件存储名称不能为空");
        checkNotNull(filepath,"需要上传的文件路径不能为空");
        return  upload(filename,path,new File(filepath));
    }

    /**
     * 上传文件
     * @param filename 上传文件存储名称
     * @param path 上传文件存储路径,为空或空字符表示上传到ftp主目录
     * @param file 需要上传的文件
     * @return
     */
    public boolean upload(String filename,String path,File file){
        checkNotNull(filename,"上传文件存储名称不能为空");
        checkNotNull(file,"需要上传的文件不能为空");
        boolean status = false;
        try {
            status=upload(filename, path, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("需要上传的文件不存在",e);
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 上传图片自动压缩处理
     * @param path 上传文件存储路径,为空或空字符表示上传到ftp主目录
     * @param filename 上传文件存储名称不能为空
     * @param inputStream 上传图片输入流
     * @param quality 上传图片压缩质量
     * @param maxWidth 图片压缩的最大宽度
     * @param maxHeight 图片压缩的最大高度
     * @return
     */
    public boolean uploadImage(String filename,String path,InputStream inputStream,float quality,int maxWidth,int maxHeight){
        checkNotNull(filename,"上传图片存储名称不能为空");
        checkNotNull(inputStream,"上传图片输入流不能为空");
        String          formatInFile          = getImgFormatName(inputStream);
        if(null==formatInFile){
            log.error("请上传图片!");
            return false;
        }
        boolean status;
        File file = new File(ImgCacheDir);
        File imagePath=null;
        if(file.exists() && file.isDirectory()){
            imagePath=new File(ImgCacheDir + filename);
        }else{
            log.error("压缩图片本地缓存文件夹不存在");
            return false;
        }
        //System.out.println(suffex);
        if(!imagePath.exists()){ //缓存目录中不存在文件则进行本地压缩缓存
            try {
                Thumbnails.of(inputStream).outputQuality(quality).size(maxWidth,maxHeight).toFile(imagePath);
            } catch (IOException e) {
                log.error("图片压缩异常",e);
                e.printStackTrace();
                return false;
            }
        }
        try {
            status=upload(filename,path,new FileInputStream(imagePath));
        } catch (FileNotFoundException e) {
            log.error("压缩图片不存在",e);
            e.printStackTrace();
            return false;
        }
        if (status) {
            imagePath.delete();// 上传成功删除缓存压缩后的图片文件
        }
        return status;
    }

/*    private static String getFormatInImgFile(InputStream inputStream) {
        return getFormatInImgFile(inputStream);
    }*/

    /**
     * @Author qyw
     * @Description 从FTP服务器下载文件
     * @Date Created in 9:58 2018/8/14
     * @Param [remotePath, fileName, localPath] fileName:要下载的文件名
     * @Return boolean
     **/
    public boolean downloadFile(String remotePath,String fileName,String localPath){
        boolean status=false;
        if (cd(remotePath)) {
            FTPFile[] ftpFiles;
            ftpClient.enterLocalPassiveMode();
            try {
                ftpFiles = ftpClient.listFiles();
            } catch (IOException e) {
                log.error("获取文件数组失败,ftp应答消息:{}",ftpClient.getReplyString());
                e.printStackTrace();
                return false;
            }
            checkArgument(ftpFiles!=null && ftpFiles.length>0,"远程文件夹为空,ftp应答消息:{}",ftpClient.getReplyString());
            System.out.println(Arrays.toString(ftpFiles));
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(fileName)) {
                    OutputStream fos=null;
                    try {
                        fos = new FileOutputStream(localPath+ File.pathSeparator+fileName);
                        ftpClient.setBufferSize(1024);
                        ftpClient.setControlEncoding(controlEncoding);
                        try {
                            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                            status=ftpClient.retrieveFile(ftpFile.getName(),fos);
                        } catch (IOException e) {
                            status=false;
                            log.error("文件下载异常,ftp应答消息:{},异常:{}",ftpClient.getReplyString(),e);
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        status=false;
                        log.error("本地文件创建失败",e);
                        e.printStackTrace();
                    }finally {
                        close();
                        if(null != fos){
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }else{
            log.error("切换工作目录失败,目录:{},ftp应答消息:{}",remotePath,ftpClient.getReplyString());
            return false;
        }
        return status;
    }




    /**
     * 切换目录
     * @param dir
     * @return
     */
    public boolean cd(String dir){
        if(null==dir){
            return true;
        }
        if(Objects.equals("",dir.trim())){
            return true;
        }
        boolean status;
        try {
            status= ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
            log.error("切换路径异常,路径:{},异常响应:{}",dir,ftpClient.getReplyString());
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
        boolean status;
        try {
            status = ftpClient.changeToParentDirectory() && ftpClient.makeDirectory(dir);
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
        boolean status;
        try {
            if(ftpClient.changeToParentDirectory()){
                for(String dirss : dirs){
                    status= ftpClient.makeDirectory(dirss) && ftpClient.changeWorkingDirectory(dirss);
                    if(!status){
                        return false;
                    }
                }
            }else{
                return false;
            }
            status=ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            log.error("创建多个层级目录异常",e);
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     * 删除文件夹
     * @param pathname 文件夹路径
     * @return
     */
    public boolean rmdir(String pathname){
        try{
            return ftpClient.removeDirectory(pathname);
        }catch(IOException e){
            log.error("删除文件夹异常",e);
            return false;
        }
    }

    /**
     * 删除文件
     * @param pathname 文件路径
     * @return
     */
    public boolean remove(String pathname){
        boolean status;
        try{
            status = ftpClient.deleteFile(pathname);
        }catch(IOException e){
            log.error("删除文件异常",e);
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

 /*   *//**
     * 检查传入file的format名称
     * @param f 传入的文件
     * @return
     *//*
    public static String getFormatInImgFile(File f) {
        return getImgFormatName(f);
    }
    *//**
     * 检查传入file的format名称
     * @param filePath 传入的文件的路径
     * @return
     *//*
    public static String getFormatInImgFile(String  filePath) {
        return getFormatInImgFile(new File(filePath));
    }*/

    /**
     * 对文件进行format检索
     * .jpg .jpeg .jpe .jfif ===> JPEG
     * .png ===> png
     * .gif ===> gif
     * .
     * @param o
     * @return
     */
    // Returns the format name of the image in the object 'o'.
    // Returns null if the format is not known.
    private static String getImgFormatName(Object o) {
        ImageInputStream iis=null;
        try {
            // Create an image input stream on the image
             iis = ImageIO.createImageInputStream(o);
            // Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }
            // Use the first reader
            ImageReader reader = (ImageReader) iter.next();
            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
           log.error("上传图片异常",e);
        }finally {
            if(iis!=null){
                try {
                    iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // The image could not be read
        return null;
    }
}
