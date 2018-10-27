package com.hnu.scw.servlet;

import com.hnu.scw.model.MyTestMessage;
import com.hnu.scw.translation.TranslationUtils;
import com.hnu.scw.utils.CheckConnectUtils;
import com.hnu.scw.utils.MessageUtils;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "ConnectWeChatServlet")
public class ConnectWeChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doGet(request ,response);
        backTestFunction(request , response);
    }

    /**
     * 进行验证是否身份匹配
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        System.out.println(""+signature +"@"+timestamp +"$"+nonce +"^"+echostr);
        PrintWriter out = response.getWriter();
        if(CheckConnectUtils.checkConncetWithWeChat(signature,timestamp,nonce)){
            out.print(echostr);
        }
    }

    /**
     * 文字回复功能开发
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void backTestFunction(HttpServletRequest request , HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try{
            Map<String , String> map = MessageUtils.xmlToMap(request);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            String message = null ;
            if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
                //进行关键字回复功能
                if("1".equals(content)){
                    message = MessageUtils.initText(fromUserName,toUserName,MessageUtils.inputNumber1());
                }else if("2".equals(content)){
                    message = MessageUtils.initText(fromUserName,toUserName,MessageUtils.inputNumber2());
                }
                else if("3".equals(content)){
                    //客户端输入“3”，返回一条图文消息
                    message = MessageUtils.initNewSMessage(fromUserName,toUserName);
                }else if("4".equals(content)){
                    //客户端输入“4”，返回一条图片消息
                    message = MessageUtils.initImageMessage(fromUserName,toUserName);
                }else if("5".equals(content)){
                    //客户端输入“5”，返回一首音乐消息
                    message = MessageUtils.initMusicMessage(fromUserName,toUserName);
                }else if(content.startsWith("翻译")){
                    //客户端输入“以翻译开头”，返回对应的翻译信息
                    String translateResult = TranslationUtils.translate(content.substring(2,content.length()));
                    message = MessageUtils.initText(fromUserName,toUserName , translateResult);
                }else {
                    message = MessageUtils.initText(fromUserName,toUserName,"你发送的消息是:" + content);
                }
            }else if(MessageUtils.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                //完成订阅时候返回的内容
                if(MessageUtils.MESSAGE_SUBSCRIBE .equals(eventType)){
                    message = MessageUtils.initText(fromUserName,toUserName , MessageUtils.menuText());
                }else if(MessageUtils.MESSAGE_CLICK .equals(eventType)){
                    //进行的是click按钮的点击事件（这里就推送一下主菜单内容）
                    message = MessageUtils.initText(fromUserName,toUserName , MessageUtils.menuText());
                }else if(MessageUtils.MESSAGE_VIEW .equals(eventType)){
                    //进行的是view按钮的点击事件（这里就推送一下主菜单内容）
                    String viewUrl = map.get("EventKey");
                    message = MessageUtils.initText(fromUserName,toUserName , viewUrl);
                }else if(MessageUtils.MESSAGE_SCANCODE .equals(eventType)) {
                    //进行的是扫码事件
                    String key = map.get("EventKey");
                    message = MessageUtils.initText(fromUserName,toUserName , key);
                }
            }else if(MessageUtils.MESSAGE_LOCATION .equals(msgType)) {
                //进行的是地理位置信息
                String label = map.get("Label");
                message = MessageUtils.initText(fromUserName,toUserName , label);
            }
            //打印输出的xml格式内容，方便进行调试
            System.out.println(message);
            out.print(message);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }
}
