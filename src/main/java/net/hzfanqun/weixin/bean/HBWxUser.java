package net.hzfanqun.weixin.bean;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;
import java.util.UUID;

@Table("weixin_user")
@TableIndexes({@Index(name = "guid", fields = {"guid"}, unique = true),
               @Index(name = "openid", fields = {"openId"}, unique = true)})
public class HBWxUser {

    @Id
    @Column("id")
    protected int id;

    // weixin openId
    @Name
    @Column("openid")
    private String openId;

    // HB 账号
    @Column("hb_username")
    private String hbUsername;

    // 微信 用户的昵称
    @Column("nick_name")
    private String nickName;

    @Column("archived")
    protected boolean archived;

    @Column("version")
    protected int version;
    /**
     * Domain business guid.
     */
    @Column("guid")
    protected String guid = generate();

    /**
     * The domain create time.
     */
    @Column("create_time")
    protected Date createTime = new Date();

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getHbUsername() {
        return hbUsername;
    }

    public void setHbUsername(String hbUsername) {
        this.hbUsername = hbUsername;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getGuid() {
        return guid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public HBWxUser() {}

}
