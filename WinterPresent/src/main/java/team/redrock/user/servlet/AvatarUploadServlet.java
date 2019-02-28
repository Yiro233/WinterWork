package team.redrock.user.servlet;


import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.AvatarUploadService;
import team.redrock.user.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


@MultipartConfig
public class AvatarUploadServlet extends HttpServlet {
    LoginService loginService;
    AvatarUploadService avatarUploadService;

    @Override
    public void init() {
        loginService = LoginService.getInstance();
        avatarUploadService=AvatarUploadService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String basePath=this.getServletContext().getRealPath("/avatar");
        File path = new File(basePath);
        if (!path.exists()){
            path.mkdir();
        }
        HttpSession session = request.getSession();
        PrintWriter out=response.getWriter();
        String username=String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        String nickname= user.getNickname();
        int j=0;
        Part img = request.getPart("avatar");
        String fileName = img.getSubmittedFileName();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = new Date().getTime() + suffix;
        File file = new File(basePath, newFileName);
        String filePath=basePath+File.separator+newFileName;


        img.write(filePath);
        String src="http://www.miyamoto.top/avatar";
        String srcPath=src+File.separator+newFileName;
        avatarUploadService.addAvatar(nickname,srcPath);
            out.write("<html>"
                    + "<head><script type='text/javascript'>alert('上传成功');location='personInfo.html';</script></head>"
                    + "<body><body></html>");
        out.print(srcPath);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
