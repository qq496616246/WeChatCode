package com.hnu.scw.menu;

/**
 * @author scw
 * @create 2018-01-17 17:20
 * @desc 最基础的Button
 **/
public class BaseButton {
    private String type;
    private String name;
    //子按钮（也可以理解为二级菜单）
    private BaseButton[] sub_button;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseButton[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(BaseButton[] sub_button) {
        this.sub_button = sub_button;
    }
}
