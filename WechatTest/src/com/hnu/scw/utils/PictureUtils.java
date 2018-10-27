package com.hnu.scw.utils;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author scw
 * @create 2018-01-17 15:17
 * @desc 用于图片或者缩略图或者视频或者音频相关的工具类
 **/
public class PictureUtils {
    //微信上传图片或者缩略图或者视频的接口URL
    private static final String UPLOADIMG_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 上传本地图片或者缩略图或者视频或者音频（这个只要取决于参数type是什么）到微信的素材管理中
     * @param fileUrl
     * @param accesstoken
     * @param type
     * @return
     * @throws IOException
     */
    public static String upLoadImage(String fileUrl,String accesstoken,String type) throws IOException{
        //创建一个文件file
        File file = new File(fileUrl);
        //判断file文件是否为空
        if(file==null){
            throw new IOException("文件不存在");
        }
        String url = UPLOADIMG_URL.replace("ACCESS_TOKEN", accesstoken).replace("TYPE",type);
        URL urlobj = new URL(url);
        urlobj.openStream();
        //httpURLConnection实例的作用是用来做一个请求但潜在网络连接到HTTP服务器
        HttpURLConnection urlconnection = (HttpURLConnection) urlobj.openConnection();
        //进行urconnection对象设置
        urlconnection.setRequestMethod("POST");
        urlconnection.setDoInput(true);
        urlconnection.setDoOutput(true);
        urlconnection.setUseCaches(false);
        //设置请求头信息
        urlconnection.setRequestProperty("Connection", "Keep-Alive");
        urlconnection.setRequestProperty("Charset", "UTF-8");
        //设置边界
        //currentTimeMillis方法获取当前时间信息
        String BOUNDARY = "-----------"+System.currentTimeMillis();
        //Content-Type，内容类型一般是指网页中存在的Content-Type，用于定义网络文件的类型和网页的编码
        //multipart/from-data请求文件上传类型
        urlconnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        //Content-Disposition就是当用户请求所得内容存为一个文件的提供一个默认的文件名
        sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
        //application.octet-stream 只能提交二进制，而且提交一个二进制，如果提交文件的话，只能提交一个文件
        //后台接收参数只能有一个，而且还只能是流或者是字节码
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        //创建一个byte数组
        //sb对象数据转换成字节码
        byte[] head = sb.toString().getBytes("utf-8");
        //获取输出流   getoutputStream作用就是返回使用此连接的流
        //OutputStream 该抽象类是所有类的字节输出流的父类
        //DataOutputStream 创建一个新的数据输出流，以便将数据写入指定的基础输出流,返回为零
        OutputStream output = new DataOutputStream(urlconnection.getOutputStream());
        //在将字节码数据转入到流对象中
        output.write(head);
        //文件正文部分
        //把文件一流文件的方式 推入url中
        //DateinputStream的作用就是file目录的文件以流的方式输入进来
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte [] b = new byte[1024];
        while((bytes=in.read(b))!=-1){
            output.write(b, 0, bytes);
        }
        //关闭输入流
        in.close();
        //结尾部分
        byte []foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8"); //定义最后数据分割线
        //把定义最后的数据分割线字节码数据转入流对象中
        output.write(foot);
        //刷新
        output.flush();
        //关闭
        output.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
        //定义一个BufferRader输入流来读取url的响应
            reader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
            System.out.println(urlconnection.getInputStream());
            String line = null;
            //while循环读取文字
            while((line=reader.readLine())!=null){
                buffer.append(line);
            }
            if(result == null){
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //关闭流
            if(reader!=null){
                reader.close();
                System.out.println("关闭");
            }
        }
        JSONObject jsonobject = JSONObject.fromObject(result);
        System.out.println(jsonobject.toString());
        //判断传入的数据格式是什么，因为不同的数据，json返回的结果集也不一样
        String jsonData = "";
        if("image".equals(type)){
            //如果传入的是图片，那么结果集的ID，就是media_id
            jsonData = "media_id";
        }else if("thumb".equals(type)){
            //如果是传的缩略图，那么就结果集的ID，就是thumb_media_id
            jsonData = type + "_media_id";
        }
        String mediaid = jsonobject.getString(jsonData);
        return mediaid;
    }
}
