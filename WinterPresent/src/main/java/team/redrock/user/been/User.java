package team.redrock.user.been;

import java.util.Date;

public class User {

    private String name;
    private String password;
    private String avatar;
    private String nickname;
    private String self_introduction;
    private int follow;
    private int followed;
    private int blog_num;
    private int collection_num;

    public void setCollection_num(int collection_num) { this.collection_num = collection_num; }

    public int getCollection_num() { return collection_num; }

    public void setBlog_num(int blog_num) { this.blog_num = blog_num; }

    public int getBlog_num() { return blog_num; }

    public void setFollow(int follow) { this.follow = follow; }

    public void setFollowed(int followed) { this.followed = followed; }

    public int getFollow() {
        return follow;
    }

    public int getFollowed() {
        return followed;
    }

    public User() {
        this.avatar="http://120.78.155.34/IMG_2976.JPG";
        this.nickname=new Date().getTime()+"用户";
        this.self_introduction="这个人很懒，什么都没留下";
        this.follow=0;
        this.followed=0;
        this.blog_num=0;
        this.collection_num=0;
    }


    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setSelf_introduction(String self_introduction) { this.self_introduction = self_introduction; }

    public String getNickname() { return nickname; }

    public String getSelf_introduction() { return self_introduction; }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [name=" +name+ ",password=" +password+  "]";
    }

}
