package team.redrock.messageBoard.dao;

import team.redrock.messageBoard.been.Message;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageBoardDao {
    private static MessageBoardDao instance=null;
    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String URL="jdbc:mysql://localhost:3306/user_info?characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";
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


    public static MessageBoardDao getInstance() {
        if (instance == null) {
            synchronized (MessageBoardDao.class) {
                if (instance == null) {
                    instance = new MessageBoardDao();
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


    //插入留言 并更新微博数,评论数
    public int insertMessage(Message message, User user) {
        int i=0;
        message.setAvatar(user.getAvatar());
        Connection con = getConnection();
        PreparedStatement ps = null;
        String sql1 = "INSERT INTO messageboard(user_nickname,date,video,picture,content,pid,`like`,avatar,content_num) VALUE(?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql1);
            ps.setString(1, message.getUser_nickname());
            ps.setString(2, message.getDate());
            ps.setString(3,message.getVideo());
            ps.setString(4, message.getPic());
            ps.setString(5, message.getContent());
            ps.setInt(6, message.getPid());
            ps.setInt(7,message.getLike());
            ps.setString(8,message.getAvatar());
            ps.setInt(9,message.getContent_num());
            i=ps.executeUpdate();

            if(message.getPid()==0){
                user.setBlog_num(user.getBlog_num()+1);
                String sql2="update user set blog_num=? where nickname=?";
                ps=con.prepareStatement(sql2);
                ps.setInt(1,user.getBlog_num());
                ps.setString(2,user.getNickname());
                ps.execute();
            }else{
                Message message1=findById(message.getPid());
                message1.setContent_num(message1.getContent_num()+1);
                String sql3="update messageboard set content_num=? where id=?";
                ps=con.prepareStatement(sql3);
                ps.setInt(1,message1.getContent_num());
                ps.setInt(2,message.getPid());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(null,ps,con);
        }
        return i;
    }

    /**
     * 通过日期找到message
     * @param date
     * @return
     */
    public  Message findByDate(String date){
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet res = null;
        String sql="select * from messageboard where date=?";
        Message message=new Message();
        try {
            ps=con.prepareStatement(sql);
            ps.setString(1,date);
            res= ps.executeQuery();
            if(res.next()){
                message.setId(res.getInt(1));
                message.setAvatar(res.getString(2));
                message.setUser_nickname(res.getString(3));
                message.setDate(res.getString(4));
                message.setVideo(res.getString(5));
                message.setPic(res.getString(6));
                message.setContent(res.getString(7));
                message.setPid(res.getInt(8));
                message.setLike(res.getInt(9));
                message.setContent_num(res.getInt(10));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res,ps,con);
        }
        return message;
    }

    /**
     * 通过id找message
     * @param id
     * @return
     */
    public Message findById(int id) {
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet res = null;
        String sql="select * from messageboard where id=? order by date desc";
        Message message=new Message();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            res= ps.executeQuery();
            if(res.next()){
                message.setId(res.getInt(1));
                message.setAvatar(res.getString(2));
                message.setUser_nickname(res.getString(3));
                message.setDate(res.getString(4));
                message.setVideo(res.getString(5));
                message.setPic(res.getString(6));
                message.setContent(res.getString(7));
                message.setPid(res.getInt(8));
                message.setLike(res.getInt(9));
                message.setContent_num(res.getInt(10));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res,ps,con);
        }
        return message;
    }


    /**
     * 查出父节点为pid的留言的集合
     *
     * @param pid 留言的父节点
     * @return 留言的集合
     */
    public List<Message> findMessagesByPid(int pid) {
        String sql = "SELECT * FROM messageboard WHERE pid = ? order by date desc";
        Connection con = getConnection();
        ResultSet res = null;
        PreparedStatement ps=null;
        List<Message> list = new ArrayList<Message>();

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, pid);
            res = ps.executeQuery();
            while (res.next()) {
                Message message = new Message();
                message.setId(res.getInt("id"));
                message.setPid(res.getInt("pid"));
                message.setDate(res.getString("date"));
                message.setVideo(res.getString("video"));
                message.setPic(res.getString("picture"));
                message.setContent(res.getString("content"));
                message.setUser_nickname(res.getString("user_nickname"));
                message.setLike(res.getInt("like"));
                message.setAvatar(res.getString("avatar"));
                message.setContent_num(res.getInt("content_num"));
                list.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(res, ps, con);
        }
        return list;
    }

    /**
     * 查出个人的微博
     * @param nickname
     * @param pid
     * @return
     */
    public List<Message> findMessagesByName(String nickname,int pid) {
        String sql = "SELECT * FROM messageboard WHERE user_nickname=? and pid = ? order by date desc";
        Connection con = getConnection();
        ResultSet res = null;
        PreparedStatement ps=null;
        List<Message> list = new ArrayList<Message>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,nickname);
            ps.setInt(2, pid);
            res = ps.executeQuery();
            while (res.next()) {
                Message message = new Message();
                message.setId(res.getInt("id"));
                message.setPid(res.getInt("pid"));
                message.setVideo(res.getString("video"));
                message.setDate(res.getString("date"));
                message.setPic(res.getString("picture"));
                message.setContent(res.getString("content"));
                message.setUser_nickname(res.getString("user_nickname"));
                message.setLike(res.getInt("like"));
                message.setAvatar(res.getString("avatar"));
                message.setContent_num(res.getInt("content_num"));
                list.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(res, ps, con);
        }
        return list;
    }




    /**
     * 判断 点赞 还是 取消点赞
     * @param message
     * @param username
     * @return
     */
    public boolean judge(Message message,String username){
    Connection conn = getConnection();
    PreparedStatement ps = null;
    ResultSet res = null;
    int num=0;
    boolean flag;
    String sql="SELECT count(username) as flag FROM `like` where message_id=? and username=?;";
    try {
        ps = conn.prepareStatement(sql);
        ps.setInt(1,message.getId());
        ps.setString(2,username);
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
     * 点赞
     * @param message
     * @param username
     * @return
     */
    public String like(Message message,String username){
    Connection conn = getConnection();
    boolean flag=judge(message,username);
    PreparedStatement ps = null;
    String result="like";
    //赞和取消赞
    if(flag){
        message.setLike((message.getLike())-1);
        result="cancel";
    }else{
        message.setLike((message.getLike())+1);
    }
    String sql1="update messageboard set `like`=? where id=?";
    String sql2="insert into `like` (message_id,username) VALUE(?,?)";
    try {
        ps = conn.prepareStatement(sql1);
        ps.setInt(1,message.getLike());
        ps.setInt(2,message.getId());
        ps.execute();

        ps=conn.prepareStatement(sql2);
        ps.setInt(1,message.getId());
        ps.setString(2,username);
        ps.execute();
    } catch (SQLException e) {
        e.printStackTrace();
    }finally {
        close(null,ps,conn);
    }
    return result;
}

    /**
     * 获取收藏的内容
     * @param user
     * @return
     */
    public List<Message> collectInfo(User user){
        int message_id;
        Message message=new Message();
        String sql="select DISTINCT message_id from collection where username=?";
        boolean flag;
        PreparedStatement ps=null;
        Connection con = getConnection();
        ResultSet res = null;
        List<Message> list = new ArrayList<Message>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,user.getName());
            res = ps.executeQuery();
            while(res.next()){
                message_id=res.getInt("message_id");
                message=new MessageBoardDao().findById(message_id);
                flag=new UserDao().decide(user,message);
                if(flag){
                    list.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(res,ps,con);
        }
        return list;
    }

    /**
     * 获取发微时间
     * @return
     */
    public String getTime(){
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
    String date=dateFormat.format(new Date());
    return date;
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

