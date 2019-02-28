package team.redrock.messageBoard.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.user.been.User;

import java.util.List;

public class MessageBoardService {
    private MessageBoardDao messageBoardDao = null;
    private static MessageBoardService instance = null;

    public MessageBoardService() {
        this.messageBoardDao = MessageBoardDao.getInstance();
    }

    public static MessageBoardService getInstance() {

        if (instance == null) {
            synchronized (MessageBoardService.class) {
                if (instance == null) {
                    instance = new MessageBoardService();
                }
            }
        }
        return instance;
    }

    private List<Message> findContentChild(Message content) {
        List<Message> list = messageBoardDao.findMessagesByPid(content.getId());
        for (Message message : list) {
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }
        return list;
    }

    /**
     * 查找所有微博和评论
     * @return
     */
    public List<Message> findAllMessages() {

        List<Message> list = messageBoardDao.findMessagesByPid(0);
        for (Message message : list) {
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }
        return list;
    }

    /**
     * 查找用户发的微博
     * @param nickname
     * @return
     */
    public List<Message> findMyMessages(String nickname) {

        List<Message> list = messageBoardDao.findMessagesByName(nickname,0);
        for (Message message : list) {
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }
        return list;
    }


    /**
     * 查找收藏的微博
     * @param user
     * @return
     */
    public List<Message> findCollection(User user){
        List<Message> list=messageBoardDao.collectInfo(user);
        for (Message message : list) {
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }
        return list;
    }

    public String messagesToJson(List<Message> messageList){
        StringBuffer sb = new StringBuffer();

        //前面的共同的部分
        sb.append("{\"status\":10000,\"data\":[");

        //如果它有子节点
        if (messageList != null && messageList.size() != 0) {

            //这里的思想和上面的思想一样 深度优先遍历(DFS) 组装出来评论的json
            for (Message content : messageList) {
                sb.append(createJson(content));
                sb.append(",");
            }

            if (sb.charAt(sb.length() - 1) == ',') {
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        sb.append("]}");

        return sb.toString();
    }

    private String createJson(Message message) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"id\":").append(message.getId())
                .append(",\"avatar\":\"").append(message.getAvatar())
                .append("\",\"user_nickname\":\"").append(message.getUser_nickname())
                .append("\",\"date\":\"").append(message.getDate())
                .append("\",\"video\":\"").append(message.getVideo())
                .append("\",\"pic\":\"").append(message.getPic())
                .append("\",\"content\":\"").append(message.getContent())
                .append("\",\"pid\":\"").append(message.getPid())
                .append("\",\"like\":\"").append(message.getLike())
                .append("\",\"content_num\":\"").append(message.getContent_num())
                .append("\"").append(",\"child\":[");
        List<Message> child = message.getChildContent();
        for (Message content : child) {
            String json = createJson(content);
            sb.append(json).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * 分批发送 每次显示6个微博
     * @param res
     * @param num
     * @return
     */
    public String PartSend(String res,int num){
        StringBuffer sb = new StringBuffer();
        JSONObject json = JSONObject.fromObject(res);
        JSONArray array=json.getJSONArray("data");
        sb.append("{\"status\":10000,\"data\":[");

        for (int i=num;i<num+6;i++){
            if(i>=array.size()){
                break;
            }
            sb.append(array.getString(i));
            sb.append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * 插入微博或留言
     * @param message
     * @param user
     * @return
     */
    public boolean insertContent(Message message, User user) {
        if (message.getUser_nickname() != null && message.getContent() != null) {
            int i=messageBoardDao.insertMessage(message,user);
            if(i==1){
                return true;
            }
        }
        return false;
    }

    /**
     * 通过id找message
     * @param id
     * @return
     */
    public Message findById(int id){
        Message message=messageBoardDao.findById(id);
        return message;
    }

    /**
     * 通过时间找 message
     * @param date
     * @return
     */
    public Message findByDate(String date){
        Message message=messageBoardDao.findByDate(date);
        return message;
    }

    /**
     * 判断点赞 还是 取消赞
     * @param message
     * @param username
     * @return
     */
    public boolean judge(Message message,String username){
        boolean flag=messageBoardDao.judge(message,username);
        return flag;
    }

    /**
     * 点赞
     * @param message
     * @param username
     * @return
     */
    public String like(Message message,String username){
        String res=messageBoardDao.like(message,username);
        return res;
    }

    public String getTime(){
        String date=messageBoardDao.getTime();
        return date;
    }



}
