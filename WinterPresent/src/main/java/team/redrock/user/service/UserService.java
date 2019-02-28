package team.redrock.user.service;


import team.redrock.messageBoard.been.Message;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;

import java.util.List;

public class UserService {
    private UserDao userDao=null;
    private static UserService instance=null;
    public UserService (){this.userDao=UserDao.getInstance();}

    public static UserService getInstance(){
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    /**
     * 组装用户信息
     * @param user
     * @return
     */
    public String createJson(User user){
        StringBuffer sb = new StringBuffer();
        sb.append("{\"username\":\"").append(user.getName())
                .append("\",\"nickname\":\"").append(user.getNickname())
                .append("\",\"avatar\":\"").append(user.getAvatar())
                .append("\",\"self_introduction\":\"").append(user.getSelf_introduction())
                .append("\",\"follow\":").append(user.getFollow())
                .append(",\"followed\":").append(user.getFollowed())
                .append(",\"blog_num\":").append(user.getBlog_num())
                .append(",\"collect_num\":").append(user.getCollection_num())
                .append("}");
        return sb.toString();
    }


    public String UserToJson(List<User> userList){
        StringBuffer sb = new StringBuffer();

        sb.append("{\"status\":10000,\"data\":[");
        if (userList != null && userList.size() != 0) {
            for(int i=0;i<userList.size();i++){
                sb.append(createJson(userList.get(i)));
                sb.append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
