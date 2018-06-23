package com.ihome.entirty;

/**
 * 用户图片上传实体类
 * user：xsy
 * date:2018-01-30
 * */
public class UserFile {

    public String imgPath;
    public Integer userId;

    public Integer  type;

    public String getImgPath () {
        return imgPath;
    }

    public void setImgPath ( String imgPath ) {
        this.imgPath = imgPath;
    }

    public Integer getUserId () {
        return userId;
    }

    public void setUserId ( Integer userId ) {
        this.userId = userId;
    }

    public Integer getType () {
        return type;
    }

    public void setType ( Integer type ) {
        this.type = type;
    }
}
