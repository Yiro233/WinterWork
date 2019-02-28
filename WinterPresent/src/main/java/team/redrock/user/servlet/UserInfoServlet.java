package team.redrock.user.servlet;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.LoginService;
import team.redrock.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


public class UserInfoServlet extends HttpServlet {
    LoginService loginService;
    UserService userService;

    @Override
    public void init() {
        loginService = LoginService.getInstance();
        userService=UserService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username= String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        String res=userService.createJson(user);
        out.write(res);
        out.close();
    }
}
