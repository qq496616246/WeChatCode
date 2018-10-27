package com.hnu.scw.menu;

/**
 * @author scw
 * @create 2018-01-17 17:23
 * @desc 自定义菜单的实体
 **/
public class CustomeMenu {
    //对菜单按钮进行封装
    private BaseButton[] button;

    public BaseButton[] getButton() {
        return button;
    }

    public void setButton(BaseButton[] button) {
        this.button = button;
    }
}
