package com.hnu.scw.model;

/**
 * @author scw
 * @create 2018-01-17 15:09
 * @desc 图片消息
 **/
public class ImageMessage extends BaseMessage {
    private ImageBase Image ;

    public ImageBase getImageBase() {
        return Image;
    }

    public void setImageBase(ImageBase Image) {
        this.Image = Image;
    }
}
