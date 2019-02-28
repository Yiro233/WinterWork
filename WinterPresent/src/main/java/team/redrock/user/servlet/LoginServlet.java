package team.redrock.user.servlet;

import team.redrock.user.been.User;
import team.redrock.user.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;


public class LoginServlet extends HttpServlet {
        LoginService loginService;

    @Override
    public void init() {
        loginService = LoginService.getInstance();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String username=request.getParameter("username");
        String password=request.getParameter("password");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        session.setAttribute("loginName", username);

        User  user=loginService.findUser(username);

        if(loginService.checkName(user,username)) {
            if (loginService.checkPassword(user,password)) {
                session.setAttribute("nickname",user.getNickname());
                Cookie cookie = new Cookie("username", URLEncoder.encode(username, "UTF-8"));
                cookie.setMaxAge(120);
                response.addCookie(cookie);
                response.sendRedirect(request.getContextPath()+"/mainpage.html");
            }else{
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('密码错误!');location='weibo.html';</script></head>"
                        + "<body><body></html>");
            }
        }else{
            out.write("<html>"
                    +"<head><script type='text/javascript'>alert('不存在该用户!');location='weibo.html';</script></head>"
                    +"<body><body></html>");
        }
        out.close();
        }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        doGet(request,response);
    }
}
