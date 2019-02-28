package team.redrock.user.servlet;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.LoginService;
import team.redrock.user.service.RegistService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistServlet extends HttpServlet {
        RegistService registService;

    @Override
    public void init() {
        registService = RegistService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String username=request.getParameter("username");
        String password=request.getParameter("password");

        PrintWriter out = response.getWriter();

        User user=new User();
        user.setName(username);
        user.setPassword(password);
        boolean flag=registService.checkName(user.getName());
        if(flag) {
            out.write("<html>"
                    + "<head><script type='text/javascript'>alert('用户名已存在!');location='signup.html';</script></head>"
                    + "<body><body></html>");
            return;
        }else {
            int i=registService.addUser(user);
            if(i==1){
                out.write("<html>"
                        + "<head><script type='text/javascript'>alert('注册成功!');location='weibo.html';</script></head>"
                        + "<body><body></html>");
            }
        }
        out.close();
    }
}
