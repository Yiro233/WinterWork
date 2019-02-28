package team.redrock.user.service;

import team.redrock.messageBoard.service.MessageBoardService;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

public class LoginService {
    private UserDao userDao=null;
    private static LoginService instance = null;
    public LoginService() {
        this.userDao=UserDao.getInstance();
    }

    public static LoginService getInstance(){
        if (instance == null) {
            synchronized (LoginService.class) {
                if (instance == null) {
                    instance = new LoginService();
                }
            }
        }
        return instance;
    }

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public User findUser(String username){
        User user=userDao.findUserByName(username);
        return user;
    }

    public User findUserByNickName(String nickname){
        User user=userDao.findUserByNickName(nickname);
        return  user;
    }

    /**
     * 检查用户名
     * @param user
     * @param username
     * @return
     */
    public boolean checkName(User user,String username){
        if(user.getName()!=null&&user.getName().equals(username)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 检查密码
     * @param user
     * @param password
     * @return
     */
    public boolean checkPassword(User user,String password){
        if(user.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }
    }

}
