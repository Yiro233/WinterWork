package team.redrock.user.servlet;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.LoginService;
import team.redrock.user.service.UpdateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


public class UpdateInfoServlet extends HttpServlet {
    UpdateService updateService;
    LoginService loginService;

    @Override
    public void init() {
        updateService=UpdateService.getInstance();
        loginService = LoginService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        HttpSession session = request.getSession();
        String username= String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        String nickname=request.getParameter("nickname");
        String self_intro=request.getParameter("self_intro");



        if((nickname==null||nickname.length()==0)&&(self_intro!=null&&self_intro.length()!=0)){
            nickname=user.getNickname();
            updateService.updateInfo(nickname,self_intro,user);
            out.write("<html>"
                    + "<head><script type='text/javascript'>alert('修改成功!');location='personInfo.html';</script></head>"
                    + "<body><body></html>");
        }
       else if((self_intro==null||self_intro.length()==0)&&(nickname!=null&&nickname.length()!=0)){
            boolean flag=updateService.checkNickName(nickname);
            self_intro=user.getSelf_introduction();
            if(flag){
                updateService.updateInfo(nickname,self_intro,user);
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('修改成功!');location='personInfo.html';</script></head>"
                        + "<body><body></html>");
            }else{
                nickname=user.getNickname();
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('昵称重复!');location='personInfo.html';</script></head>"
                        + "<body><body></html>");
                updateService.updateInfo(nickname,self_intro,user);
            }
        }
      else  if((nickname!=null&&nickname.length()!=0)&&(self_intro!=null&&self_intro.length()!=0)) {
            boolean flag=updateService.checkNickName(nickname);
            if(flag){
                updateService.updateInfo(nickname,self_intro,user);
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('修改成功!');location='personInfo.html';</script></head>"
                        + "<body><body></html>");
            }else{
                nickname=user.getNickname();
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('昵称重复!');location='personInfo.html';</script></head>"
                        + "<body><body></html>");
                updateService.updateInfo(nickname,self_intro,user);
            }
        }
      else  if((self_intro==null||self_intro.length()==0)&&(nickname==null||nickname.length()==0)){
            out.write("<html>"
                    + "<head><script type='text/javascript'>alert('请输入内容!');location='personInfo.html';</script></head>"
                    + "<body><body></html>");
        }
            out.close();
    }

}
