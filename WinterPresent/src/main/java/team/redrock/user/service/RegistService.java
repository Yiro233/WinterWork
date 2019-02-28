package team.redrock.user.service;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

public class RegistService {
    private UserDao userDao=null;
    private static RegistService instance = null;
    public RegistService() {
        this.userDao=UserDao.getInstance();
    }

    public static RegistService getInstance(){
        if (instance == null) {
            synchronized (RegistService.class) {
                if (instance == null) {
                    instance = new RegistService();
                }
            }
        }
        return instance;
    }


    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    public boolean checkName(String username){
        User user=userDao.findUserByName(username);
        if(user.getName()==null){
            return false;
        }
        return true;
    }

    /**
     * 添加用户是否成功
     * @param user
     * @return
     */
    public int addUser(User user){
        return  userDao.addUser(user);
    }
}
