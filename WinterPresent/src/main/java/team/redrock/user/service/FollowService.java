package team.redrock.user.service;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

import java.util.List;

public class FollowService {
    private UserDao userDao=null;
    private static FollowService instance=null;

    public FollowService(){
        this.userDao=UserDao.getInstance();
    }

    public static FollowService getInstance(){
        if (instance == null) {
            synchronized (FollowService.class) {
                if (instance == null) {
                    instance = new FollowService();
                }
            }
        }
        return instance;
    }

    /**
     * 关注
     * @param user
     * @param follow_user
     * @return
     */
    public String follow(User user,User follow_user){
       return  userDao.follow(user,follow_user);
    }

    /**
     * 判断关注状态
     * @param username
     * @param follow_name
     * @return
     */
    public boolean judge(String username ,String follow_name){
        return userDao.judge(username,follow_name);
    }

    /**
     * 关注人信息
     * @param user
     * @return
     */
    public List<User> followInfo(User user){
        return userDao.followInfo(user);
    }
}
