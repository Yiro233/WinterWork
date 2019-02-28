package team.redrock.user.service;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

public class UpdateService {
    private UserDao userDao=null;
    private static UpdateService instance = null;

    public  UpdateService() {
        this.userDao=UserDao.getInstance();
    }

    public static UpdateService getInstance(){
        if (instance == null) {
            synchronized (UpdateService.class) {
                if (instance == null) {
                    instance = new UpdateService();
                }
            }
        }
        return instance;
    }

    public boolean checkNickName(String nickname){
        User user=userDao.findUserByNickName(nickname);
        if(user.getName()==null){
            return true;
        }
        return false;
    }

    /**
     * 更新个人信息
     * @param nickname
     * @param self_intro
     * @param user
     */
    public void updateInfo(String nickname,String self_intro,User user){
        userDao.updateInfo(nickname,self_intro,user);
    }
}
