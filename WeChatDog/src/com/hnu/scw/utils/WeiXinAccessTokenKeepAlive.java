package com.hnu.scw.utils;

import com.hnu.scw.model.AccessToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author scw
 * @create 2018-01-20 10:18
 * @desc  对于Access_Token的获取优化处理
 * 主要是减少Access_Token的访问次数，
 * 每隔两个小时的时候再重新去获取，
 * 否则就会原来的请求到的access_token值
 **/
public class WeiXinAccessTokenKeepAlive {
    /**
     * 获取access_token的值
     * @return
     */
    public static String getWeixinAccessToken() throws Exception {
        //方法一：从内存中获取到access_token的值
        /*WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext application = webApplicationContext.getServletContext();
        //判断内存中是否存储了access_token的值
        if(application.getAttribute("currentaccesstoken")!=null){
            //存在，则先获取到存储的access_token值
            AccessToken tempToken=(AccessToken) application.getAttribute("currentaccesstoken");
            //判断当前时间是否超过了获取的access_token的有效时间（之前存入的时候，做了两个小时有效时间的处理）
            if(System.currentTimeMillis()>tempToken.getExpireIn()){
                //在有效时间范围内，直接用原来的
                return tempToken.getToken();
            }else{
                //不在有效时间范围内容，重新获取最新的
                return getAccessTokenWrite2WebStore();
            }
        }else{
            //没有存在token值，那么直接进行获取s
            return getAccessTokenWrite2WebStore();
        }*/
        //========方法一结束=============================
        //方法二：从properties文件中获取
        //读取保存token的properties文件
        String tokenPropertiesName = "WeiXinAccessToken.properties";
        Properties properties = new Properties();
        //获取文件流
        InputStream is = WeiXinAccessTokenKeepAlive.class.getClassLoader().getResourceAsStream(tokenPropertiesName);
        try {
            properties.load(is);
            //判断是否token过期
            long currentTime = System.currentTimeMillis();
            long saveTime = Long.parseLong(properties.getProperty("save_maxtime"));
            //如果当前的时间大于token最大的有效时间，则进行重新获取
            if(currentTime > saveTime){
                return getAccessTokenWrite2Properties();
            }else{
                //返回保存的token内容
                return properties.getProperty("save_accesstoken");
            }
        } catch (IOException e) {
             throw new IOException("保存token的properties文件不存在");
        }
    }

    /**
     * 获取access_token的值方法一：
     * 思路：获取access_token的值，并且将获取到的值，保存到web内存中，方便多次进行获取
     * 特点：
     *     但是，该方法适用于单服务器的情况，因为保存的是在内存中，多服务器就会有问题
     * @return  返回access_token值
     */
    public static String getAccessTokenWrite2WebStore(){
        //调用获取access_token的静态方法
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //获取到一次access_token之后，将其保存到web容器的内存进行管理
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext application = webApplicationContext.getServletContext();
        //当获取到access_token之后，将当前的时间+7200秒（也就是2个小时），重新构造一个access_toekn放到内存，
        // 这样的话，以后直接比较用的时间个存入的时间是否大于即可
        accessToken.setExpireIn(System.currentTimeMillis()+accessToken.getExpireIn());
        application.setAttribute("currentaccesstoken", accessToken);

        return accessToken.getToken();
    }

    /**
     * 获取access_token的值方法二：
     * 思路：获取access_token的值，并且将该值写入根目录下面的WeiXinAccessToken.properties文件中
     * 特点：
     *      该方法主要是通过当获取到一次access_token的值之后，将对应的内容写入到WeiXinAccessToken.properties文件中
     *      这样就可以保存最近的一次的access_token的值，避免了多次重复进行获取
     *      （这个指的是2小时内，因为默认是2小时有效）如果在有效期间内，那么直接用获取到的即可，否则就再重新获取，
     *      这种方法，相对第一种方法有优化，因为不只但服务器可以，多服务器也可以，但是消耗会稍微多一点
     * @return  返回access_token的值
     */
    public static String getAccessTokenWrite2Properties(){
        //1:调用获取access_token的工具方法
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //2:将获取到的内容保存到properties文件中
        Properties properties = new Properties();
        //存入token值
        properties.setProperty("save_accesstoken" , accessToken.getToken());
        //存入有效时间段(单位：秒)
        properties.setProperty("save_expiretime" , String.valueOf(accessToken.getExpireIn()));
        //存入获取的时间(单位格式:yyyy-MM-dd HH:mm:ss)
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        properties.setProperty("save_time" , retStrFormatNowDate);
        //存入有效时间的极限值（单位：毫秒）
        //这里的含义是：获取的时间毫秒+7200*1000----》等价：
        // 在当前时间后的两个小时，也可以控制为1.5小时，这样相对保险（所以，我这就相当于1.5小时获取一次）
        properties.setProperty("save_maxtime" , String.valueOf(System.currentTimeMillis() + 5400 * 1000));
        //将properties对象写入
        String tokenPropertiesName = "WeiXinAccessToken.properties";
        //获取到路径
        URL resource = WeiXinAccessTokenKeepAlive.class.getClassLoader().getResource(tokenPropertiesName);
        try {
            //写入内容到根目录
            FileOutputStream fileOutputStream = new FileOutputStream(new File(resource.toURI()));
            properties.store(fileOutputStream , null);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回access_token
        return accessToken.getToken();
    }

    /**
     * 获取access_token的值方法三：
     * 思路：获取access_token的值，并且将该值保存到数据库中
     * 特点：这种方式的话，就是当获取到access_token的值之后，将该值保存到数据库相应的一个表中
     *      当需要的时候，就取出来，然后判断是否过时间，其实这个类似第二种方法，只是换了一种存储形式
     *      并且数据库的话，利用带数据缓存的数据库，比如redis，这样的效果也不错
     *      具体的步骤就不写了，思路应该很简单，就是添加和更新操作即可
     * @return  返回access_token的值
     */

    /**
     * 获取access_token的值方法四：
     * 思路：通过spring内置的定时任务来进行定时的获取access_token的值
     * 特点：这种方式类似下面的第五种方法，这个主要是通过spring内置的定时任务，
     *      来设置一定的时间间隔，从而保证Access_token是有效的
     * @return  返回access_token的值
     */
    /*@Component
    public class TaskScheduledTest {
         * 设置定时器的处理方法和处理间隔时间，时间单位是毫秒
         * 要使用的话，记得在spring配置文件中加入<task:annotation-driven/>属性，进行自动扫描
        @Scheduled(fixedRate = 1000)
        public void printTest(){
            //这里面再进行获取access_token的处理即可。
            System.out.println(System.currentTimeMillis());
        }
    }*/

    /**
     * 获取access_token的值方法五：
     * 思路：获取access_token的值，通过（单例模式和子线程开启）来实现
     * 特点：这种方式的话，就是利用单例模式的特点，就是将该access_token用单例对象封装
     *      保证容器中，只存在一个对象，从而实现一定时间内，不进行重复获取
     * @return  返回access_token的值
     */
    /*public class SingleAccessToken {
        private AccessToken accessToken;
        private static SingleAccessToken singleAccessToken;
        private SingleAccessToken(){
            accessToken = WeiXinUtils.getAccessToken();
            //通过线程进行获取
            initThread();
        }
        //获取SingleAccessToken对象(单例模式)
        public static SingleAccessToken getInstance(){
            if(singleAccessToken==null){
                singleAccessToken=new SingleAccessToken();
            }
            return singleAccessToken;
        }
        public AccessToken getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }
        // 开启线程，设置SingleAccessToken为空
        private void initThread(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //睡眠7000秒(这个主要是因为access_token的默认时间是7200秒)
                        Thread.sleep(7000*1000);
                        singleAccessToken=null;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }*/
}
