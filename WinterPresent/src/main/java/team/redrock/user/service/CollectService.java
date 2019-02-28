package team.redrock.user.service;

import team.redrock.messageBoard.been.Message;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

public class CollectService {
    private UserDao userDao=null;
    private static CollectService instance=null;

    public CollectService(){
        this.userDao=UserDao.getInstance();
    }

    public static CollectService getInstance(){
        if (instance == null) {
            synchronized (CollectService.class) {
                if (instance == null) {
                    instance = new CollectService();
                }
            }
        }
        return instance;
    }

    public boolean decide(User user, Message message){
        return userDao.decide(user,message);
    }

    public String collect(User user,Message message){
        return userDao.collect(user,message);
    }

}
