package team.redrock.user.dao;


import team.redrock.messageBoard.been.Message;
import team.redrock.user.been.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static UserDao instance=null;
    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String URL="jdbc:mysql://localhost:3306/user_info?characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
    private static final String USER="root";
    //以下注释的为远程服务器的数据库密码，在远程服务器测试时使用
    //private static final String PASSWORD="zhanghang";

    //这个为本地测试使用
    private static final String PASSWORD="hang";

    static{
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }
        return instance;
    }

    /**
     * 数据库连接
     */

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 通过用户名找用户信息，返回user对象
     * @param name
     * @return
     */
    public User findUserByName(String name){
        String sql="select * from user where username=?";
        Connection conn=getConnection();
        ResultSet rs=null;
        PreparedStatement ps = null;
        User user=new User();
        try {
            ps=conn.prepareStatement(sql);
            ps.setString(1,name);
            rs=ps.executeQuery();
            if(rs.next()){
                //把查出的信息储存到user中
                user.setName(rs.getString(1));
                user.setPassword(rs.getString(2));
                user.setAvatar(rs.getString(3));
                user.setSelf_introduction(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setFollowed(rs.getInt(7));
                user.setFollow(rs.getInt(6));
                user.setBlog_num(rs.getInt(8));
                user.setCollection_num(rs.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
                close(rs,ps,conn);
        }
        return user;
    }

    /**
     * 通过昵称找用户
     * @param nickname
     * @return
     */
    public User findUserByNickName(String nickname){
        String sql="select * from user where nickname=?";
        Connection conn=getConnection();
        PreparedStatement ps = null;
        ResultSet rs=null;
        User user=new User();
        try {
            ps=conn.prepareStatement(sql);
            ps.setString(1,nickname);
            rs=ps.executeQuery();
            if(rs.next()){
                user.setPassword(rs.getString(2));
                user.setName(rs.getString(1));
                user.setAvatar(rs.getString(3));
                user.setSelf_introduction(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setFollow(rs.getInt(6));
                user.setFollowed(rs.getInt(7));
                user.setBlog_num(rs.getInt(8));
                user.setCollection_num(rs.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(rs,ps,conn);
        }
        return user;
    }



    /**
     * 注册添加用户
     * @param user
     * @return
     */
    public int addUser(User user){
        //sql语句
        String sql="insert into user values(?,?,?,?,?,?,?,?,?)";
        Connection conn=getConnection();
        PreparedStatement ps = null;
        int i=0;
        try {
            ps=conn.prepareStatement(sql);
            ps.setString(1,user.getName());
            ps.setString(2,user.getPassword());
            ps.setString(3,user.getAvatar());
            ps.setString(4,user.getSelf_introduction());
            ps.setString(5,user.getNickname());
            ps.setInt(6,user.getFollow());
            ps.setInt(7,user.getFollowed());
            ps.setInt(8,user.getBlog_num());
            ps.setInt(9,user.getCollection_num());
            i=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(null,ps,conn);
        }
        return i;
    }

    /**
     * 上传头像路径
     * @param nickname
     * @param path
     */
    public void addAvatar(String nickname,String path){
        String sql1="update user set avatar=? where nickname=?";
        String sql2="update messageboard set avatar=? where user_nickname=?";
        Connection conn=getConnection();
        PreparedStatement ps = null;
        try {
            ps=conn.prepareStatement(sql1);
            ps.setString(1,path);
            ps.setString(2,nickname);
            ps.executeUpdate();

            ps=conn.prepareStatement(sql2);
            ps.setString(1,path);
            ps.setString(2,nickname);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(null,ps,conn);
        }
    }


    /**
     * 修改个人信息
     * @param nickname
     * @param self_intro
     * @param user
     */
    public void updateInfo(String nickname,String self_intro,User user){

        String sql1="update user set nickname=?,self_introduction=? where username=?";
        String sql2="update messageboard set user_nickname=? where user_nickname=?";
        Connection conn=getConnection();
        PreparedStatement ps = null;
        try {
            ps=conn.prepareStatement(sql1);
            ps.setString(1,nickname);
            ps.setString(2,self_intro);
            ps.setString(3,user.getName());
            ps.execute();

            ps=conn.prepareStatement(sql2);
            ps.setString(1,nickname);
            ps.setString(2,user.getNickname());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null,ps,conn);
        }
    }

    /**
     * 判断当前状态 是关注中 还是取关中
     * @param username
     * @param follow_name
     * @return
     */
    public boolean judge(String username ,String follow_name){
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet res = null;
        int num=1;
        boolean flag;
        String sql="SELECT count(username) as flag FROM `follow` where username=? and follow_name=?;";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,follow_name);
            res = ps.executeQuery();
            if(res.next()){
                num=res.getInt("flag");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res,ps,conn);
        }
        if(num%2!=0){
            flag=true;
        }else{
            flag=false;
        }
        return flag;
    }


    //添加关注记录,更新关注数，被关注数；
    public String follow(User user,User follow_user){

        boolean flag=judge(user.getName(),follow_user.getName());
        String result="follow";
        if(flag){
            user.setFollow(user.getFollow()-1);
            follow_user.setFollowed(follow_user.getFollowed()-1);
            result="cancel";
        }else{
            user.setFollow(user.getFollow()+1);
            follow_user.setFollowed(follow_user.getFollowed()+1);
        }

        String sql1="update user set follow=? where nickname=?";
        String sql2="update user set followed=? where nickname=?";
        String sql3="insert into follow values(?,?)";
        Connection conn=getConnection();
        PreparedStatement ps = null;

        int i=0;
        try {
            ps=conn.prepareStatement(sql1);
            ps.setInt(1,user.getFollow());
            ps.setString(2,user.getNickname());
            ps.execute();

            ps=conn.prepareStatement(sql2);
            ps.setInt(1,follow_user.getFollowed());
            ps.setString(2,follow_user.getNickname());
            ps.execute();

            ps=conn.prepareStatement(sql3);
            ps.setString(2,follow_user.getName());
            ps.setString(1,user.getName());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(null,ps,conn);
        }
        return result;
    }


    /**
     * 查看关注人信息
     * @param user
     * @return
     */
    public List<User> followInfo(User user){
        String follow_name;
        String sql="select DISTINCT follow_name from follow where username=? ";
        boolean flag;
        PreparedStatement ps=null;
        Connection con = getConnection();
        ResultSet res = null;
        List<User> list = new ArrayList<User>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,user.getName());
            res = ps.executeQuery();
            while(res.next()){
                follow_name=res.getString("follow_name");
                flag=judge(user.getName(),follow_name);
                if(flag){
                    User followed=new UserDao().findUserByName(follow_name);
                    list.add(followed);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res, ps, con);
        }
        return list;
    }


    /**
     * 获取状态 收藏中 还是没收藏
     * @param user
     * @param message
     * @return
     */
    public boolean decide(User user,Message message){
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet res = null;
        int num=0;
        boolean flag;
        String sql="SELECT count(username) as flag FROM `collection` where message_id=? and username=?;";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,message.getId());
            ps.setString(2,user.getName());
            res = ps.executeQuery();
            if(res.next()){
                num=res.getInt("flag");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res,ps,conn);
        }
        if(num%2!=0){
            flag=true;
        }else{
            flag=false;
        }
        return flag;
    }

    /**
     * 收藏
     * @param user
     * @param message
     * @return
     */
    public String collect(User user,Message message){
        Connection conn = getConnection();
        boolean flag=decide(user,message);
        PreparedStatement ps = null;
        String result="collect";
        if(flag){
            user.setCollection_num(user.getCollection_num()-1);
            result="cancel";
        }else{
            user.setCollection_num(user.getCollection_num()+1);
        }
        String sql1="update user set collection_num=? where username=?";
        String sql2="insert into collection (username,message_id) VALUE(?,?)";
        try {
            ps = conn.prepareStatement(sql1);
            ps.setInt(1,user.getCollection_num());
            ps.setString(2,user.getName());
            ps.execute();

            ps=conn.prepareStatement(sql2);
            ps.setString(1,user.getName());
            ps.setInt(2,message.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(null,ps,conn);
        }
        return result;
    }



    public UserDao() {
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if(rs!=null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
