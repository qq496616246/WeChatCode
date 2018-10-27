package com.hnu.scw.utils;

import com.hnu.scw.model.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author scw
 * @create 2018-01-17 9:52
 * @desc 用户转换消息的格式
 **/
public class MessageUtils {
    /**
     * 定义多种消息类型
     */
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    //扫码事件
    public static final String MESSAGE_SCANCODE = "scancode_push";

    /**
     * XML格式转为map格式
     * @param request
     * @return
     */
    public static Map<String , String> xmlToMap(HttpServletRequest request){
        Map<String ,String> map = new HashMap<String , String>();
        try {
            InputStream inputStream =null;
            inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(inputStream);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element el:elements) {
                map.put(el.getName() , el.getText());
            }
            inputStream.close();
            return map ;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    /**
     * 文本消息对象转为xml格式
     * @param myTestMessage
     * @return
     */
    public static String textMessage2Xml(MyTestMessage myTestMessage){
        XStream xStream = new XStream();
        xStream.alias("xml" , myTestMessage.getClass());
        return xStream.toXML(myTestMessage);
    }

    /**
     * 将图文消息对象转化为图文格式的XML
     * @return
     */
    public static String newsMessage2XML(NewsMessage newsMessage){
        XStream xStream = new XStream();
        //将需要修改的一些标签进行替换
        xStream.alias("xml" , newsMessage.getClass());
        xStream.alias("item" , new NewsBase().getClass());
        return xStream.toXML(newsMessage);
    }
    /**
     * 设置需要返回的图文信息
     * @param fromUserName
     * @param toUserName
     * @return
     */
    public static String initNewSMessage(String fromUserName , String toUserName ){
        String message = null;
        List<NewsBase> newList = new ArrayList<NewsBase>();
        NewsMessage newsMessage = new NewsMessage();
        NewsBase  newsBase = new NewsBase();
        newsBase.setTitle("我是图文消息");
        newsBase.setDescription("测试测试测试测试测试测试测试测试测试");
        newsBase.setPicUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=677717294,4155848424&fm=27&gp=0.jpg");
        newsBase.setUrl("www.baidu.com");
        //添加图文消息的内容
        newList.add(newsBase);

        //注意接受消息和发送消息的顺序要反过来，因为现在是从服务器进行发送了，而客户端是接收端了
        newsMessage.setFromUserName(toUserName);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
        newsMessage.setMsgType("news");
        newsMessage.setArticleCount(newList.size());
        newsMessage.setArticles(newList);
        //调用转为图文的XML
        return MessageUtils.newsMessage2XML(newsMessage);
    }


    /**
     * 设置需要返回的文本信息
     * @param fromUserName
     * @param toUserName
     * @param content
     * @return
     */
    public static String initText(String fromUserName , String toUserName , String content){
        MyTestMessage text = new MyTestMessage();
        //注意接受消息和发送消息的顺序要烦过来
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtils.MESSAGE_TEXT);
        long time = System.currentTimeMillis();
        text.setCreateTime(String.valueOf(time));
        text.setContent(content);
        return MessageUtils.textMessage2Xml(text);
    }


    /**
     * 设置订阅时，返回菜单的内容
     * @return
     */
    public static String menuText(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("菜单1：回复数字1，有惊喜\n");
        stringBuilder.append("菜单2：回复数字2，有惊喜\n");
        stringBuilder.append("菜单3：回复数字3，有惊喜\n");
        stringBuilder.append("菜单4：回复数字3，有惊喜\n");
        stringBuilder.append("菜单5：回复数字3，有惊喜\n");
        stringBuilder.append("菜单6：回复数字未知的东东，也还有有惊喜哦\n");
        return stringBuilder.toString();
    }


    /**
     * 回复关键字"1"的时候的内容
     * @return
     */
    public static String inputNumber1(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("我是惊喜111，哈哈，惊喜不惊喜！");
        return stringBuilder.toString();
    }

    /**
     * 回复关键字"2"的时候的内容
     * @return
     */
    public static String inputNumber2(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("我是惊喜2222，哈哈，惊喜不惊喜！");
        return stringBuilder.toString();
    }

    /**
     * 返回图片消息(对于视频和音频都是一样的方式，只需要更改类型即可，即将Image修改为video，voice)
     * @param fromUserName
     * @param toUserName
     * @return
     */
    public static String initImageMessage(String fromUserName , String toUserName ){
        ImageBase imageBase = new ImageBase();
        //这个media_Id是在之前执行过上传图片返回得到的信息
        imageBase.setMediaId("HK17wQmCupESK4B9u14PqI4w3gtteXhUtGgriJW6G5c8O-Y0OsjGbYfQYhGDbYDx");
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(MessageUtils.MESSAGE_IMAGE);
        long time = System.currentTimeMillis();
        imageMessage.setCreateTime(String.valueOf(time));
        imageMessage.setImageBase(imageBase);
        return imageMessage2XML(imageMessage);
    }
    /**
     * 将图片消息对象转化为图片格式的XML
     * @return
     */
    public static String imageMessage2XML(ImageMessage imageMessage){
        XStream xStream = new XStream();
        //将需要修改的一些标签进行替换
        xStream.alias("xml" , imageMessage.getClass());
        xStream.alias("Image" , new ImageBase().getClass());
        return xStream.toXML(imageMessage);
    }



    /**
     * 将音乐消息对象转化为图片格式的XML
     * @return
     */
    public static String musicMessage2XML(MusicMessage musicMessage){
        XStream xStream = new XStream();
        //将需要修改的一些标签进行替换
        xStream.alias("xml" , musicMessage.getClass());
        xStream.alias("Music" , new MusicBase().getClass());
        return xStream.toXML(musicMessage);
    }

    /**
     * 返回音乐消息
     * @param fromUserName
     * @param toUserName
     * @return
     */
    public static String initMusicMessage(String fromUserName , String toUserName ){
        MusicBase musicBase = new MusicBase();
        //这个ThumbMediaId是在之前执行过上传缩略图返回得到的信息（这个和上传图片的方法是一样的，都是调用pictureUtils中的上传方法）
        musicBase.setThumbMediaId("vJOi5E4_U91onQnsayPdkzxted6ZctEAEzcoLd3BJ8a00gJLuhEmTckF6S2XkS5R");
        musicBase.setTitle("来一首音乐");
        musicBase.setDescription("静静的听首歌吧！");
        //设置高质量音质的歌曲路径，可以和一般音质音乐的路径一样
        musicBase.setHQMusicUrl("http://myjava.ngrok.xiaomiqiu.cn/resource/mymusic.mp3");
        //设置音乐的路径
        musicBase.setMusicUrl("http://myjava.ngrok.xiaomiqiu.cn/resource/mymusic.mp3");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        //设置类型为音乐
        musicMessage.setMsgType(MessageUtils.MESSAGE_MUSIC);
        long time = System.currentTimeMillis();
        musicMessage.setCreateTime(String.valueOf(time));
        musicMessage.setMusic(musicBase);
        return musicMessage2XML(musicMessage);
    }




}
