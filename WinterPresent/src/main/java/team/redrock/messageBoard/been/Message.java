package team.redrock.messageBoard.been;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Message {
    private int id;
    private String avatar;
    private int pid;
    private String date;
    private String video;
    private String pic;
    private String content;
    private String user_nickname;
    private int like;
    private List<Message> childContent;
    private int content_num;

    public void setContent_num(int content_num) { this.content_num = content_num; }

    public int getContent_num() { return content_num; }

    public void setVideo(String video) { this.video = video; }

    public String getVideo() { return video; }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setChildContent(List<Message> childContent) { this.childContent = childContent; }

    public List<Message> getChildContent() { return childContent; }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Message() {
    }

    public Message(String user_nickname,int pid, String date,String video,String pic, String content, int like,String avatar,int content_num) {
        this.user_nickname=user_nickname;
        this.pid = pid;
        this.date = date;
        this.video=video;
        this.pic = pic;
        this.content = content;
        this.like = like;
        this.avatar=avatar;
        this.content_num=content_num;
    }

    public int getId() {
        return id;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDate() {
        return this.date;
    }

    public String getPic() {
        return pic;
    }

    public String getContent() {
        return content;
    }

    public int getLike() {
        return like;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
