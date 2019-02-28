package team.redrock.messageBoard.servlet;

import javafx.print.Printer;
import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.messageBoard.service.MessageBoardService;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@MultipartConfig
public class SendServlet extends HttpServlet {
    MessageBoardService messageBoardService;
    LoginService loginService;
    @Override
    public void init() {
        this.messageBoardService = MessageBoardService.getInstance();
        this.loginService=LoginService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        int j=0;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        //获取图片名并进行重命名
        String img_filePath="";
        String img_srcPath="";
        String img_basePath=this.getServletContext().getRealPath("/picture");
        File img_path = new File(img_basePath);
        if (!img_path.exists()){
            img_path.mkdir();
        }
        Part img = request.getPart("picture");
        String img_fileName = img.getSubmittedFileName();
        if(img_fileName!=null&&img_fileName.length()!=0){
            String img_suffix = img_fileName.substring(img_fileName.lastIndexOf('.'));
            String img_newFileName = new Date().getTime() + img_suffix;
            File img_file = new File(img_basePath, img_newFileName);
            img_filePath=img_basePath+File.separator+img_newFileName;
            img.write(img_filePath);
            String img_src="http://www.miyamoto.top/picture";
            img_srcPath=img_src+File.separator+img_newFileName;
        }

        //获取视频名并进行重命名
        String video_filePath="";
        String video_srcPath="";
        String video_basePath=this.getServletContext().getRealPath("/video");
        File video_path = new File(video_basePath);
        if (!video_path.exists()){
            video_path.mkdir();
        }
        Part video = request.getPart("video");
        String video_fileName = video.getSubmittedFileName();
        if((video_fileName)!=null&&video_fileName.length()!=0){
            String video_suffix = video_fileName.substring(video_fileName.lastIndexOf('.'));
            String video_newFileName = new Date().getTime() + video_suffix;
            File video_file = new File(video_basePath, video_newFileName);
            video_filePath=video_basePath+File.separator+video_newFileName;
            video.write(video_filePath);
            String video_src="http://www.miyamoto.top/video";
            video_srcPath=video_src+File.separator+video_newFileName;
        }

        String username=String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        String content=request.getParameter("content");
        String idStr = request.getParameter( "pid" );
        String date=messageBoardService.getTime();
        String user_nickname=user.getNickname();
        int pid=Integer.parseInt(idStr == null || "".equals(idStr)?"0":idStr);
        Message message=new Message(user_nickname,pid,date,video_srcPath,img_srcPath,content,0,user.getAvatar(),0);
        String res="error";

        if(messageBoardService.insertContent(message,user)){
            StringBuffer sb = new StringBuffer();
            message=messageBoardService.findByDate(message.getDate());
            sb.append("{\"id\":").append(message.getId())
                    .append(",\"pid\":").append(idStr)
                    .append(",\"picture\":\"").append(img_srcPath)
                    .append("\",\"video\":\"").append(video_srcPath)
                    .append("\"}");
            res=sb.toString();
            out.write(res);
        }else{
            out.write(res);
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
