package com.hnu.scw.controller;

import com.hnu.scw.bean.WeiXinUser;
import com.hnu.scw.model.Person;
import com.hnu.scw.service.PersonService;
import com.hnu.scw.utils.WeiXinUserInfoUtiols;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author scw
 * @create 2017-12-19 16:44
 * @desc
 **/
@Controller
public class PersonController {
    @Autowired
    private PersonService personService;

    /**
     * 保存测试
     * @return
     */
    @RequestMapping(value = "/person")
    public String testDemo(){
        Person person = new Person();
        person.setName("我是8888的");
        person.setSex("男");
        personService.savePerson(person);
        return "hello";
    }

    /**
     * 更新测试
     * @return
     */
    @RequestMapping(value = "/updateperson")
    public String updateDemo(){
        Person person = new Person();
        person.setSex("哈哈");
        person.setName("我是更新操作");
        person.setId(1L);
        personService.updatePerSon(person);
        return "hello";
    }

    /**
     * 获取数据测试
     * @return
     */
    @RequestMapping(value = "getperson")
    public String loadDemo(Map<String , Object> map){
        Person person = personService.loadPersonById(2L);
        System.out.println(person);
        map.put("person" , person);
        return "hello";
    }

    /**
     * 获取特殊查询的数据条目数测试
     * @return
     */
    @RequestMapping(value = "totalperson")
    public String getTotalDemo(){
        Person person = new Person();
        person.setSex("女");
        Integer totalPersonNumber = personService.findTotalPersonNumber(person);
        System.out.println("查询的数据条目为："+totalPersonNumber);
        return "hello";
    }


    /**
     * 新增person用户
     * @param person
     * @return
     */
    @RequestMapping(value = "/addperson")
    public String addPerson(Person person){
        personService.savePerson(person);
        return "success";
    }

    /**
     * 模拟查看物流的处理
     * @return
     */
    @RequestMapping(value = "/queryproduct")
    public String queryProduct(){
        return "wuliu";
    }

    /**
     * 模拟修改地址和收获时间的处理
     * @return
     */
    @RequestMapping(value = "/updateaddress")
    public String updateAddress(){
        return "address_update";
    }

    /**
     * 模拟获取用户的信息
     * @return
     */
    @RequestMapping(value = "/tomainpage")
    public String toMainPage( HttpServletRequest request , HttpSession httpSession , Map<String , Object > map){
        String opendId = (String) httpSession.getAttribute("openid");
        String userInfo = WeiXinUserInfoUtiols.getUserInfo(opendId);
        JSONObject jsonObject = JSONObject.fromObject(userInfo);
        WeiXinUser weiXinUser = (WeiXinUser) JSONObject.toBean(jsonObject, WeiXinUser.class);
        map.put("userInfo" , weiXinUser);
        return "hello";
    }

}
