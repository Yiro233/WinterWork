package team.redrock.user.service;

import team.redrock.user.dao.UserDao;

public class AvatarUploadService {
    private UserDao userDao=null;
    private static AvatarUploadService instance=null;

    public AvatarUploadService(){
        this.userDao=UserDao.getInstance();
    }

    public static AvatarUploadService getInstance(){
        if (instance == null) {
            synchronized (AvatarUploadService.class) {
                if (instance == null) {
                    instance = new AvatarUploadService();
                }
            }
        }
        return instance;
    }

    /**
     * 上传头像
     * @param nickname
     * @param path
     */
    public void addAvatar(String nickname,String path){
        userDao.addAvatar(nickname,path);
    }



}
