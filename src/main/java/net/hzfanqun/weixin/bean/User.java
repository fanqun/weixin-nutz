package net.hzfanqun.weixin.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.weixin.bean.WxUser;

import java.util.Date;

@Table("users")
public class User extends WxUser {
    @Id
    private int id;

    @Name
    @ColDefine(width = 64)
    private String openid;

    @ColDefine(width = 500)
    private String headimgurl;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
