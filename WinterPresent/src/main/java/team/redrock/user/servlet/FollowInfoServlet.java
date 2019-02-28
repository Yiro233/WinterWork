package team.redrock.user.servlet;

import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.FollowService;
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
import java.util.List;


public class FollowInfoServlet extends HttpServlet {

    FollowService followService;
    LoginService loginService;
    UserService userService;

    @Override
    public void init() {
        loginService = LoginService.getInstance();
        followService=FollowService.getInstance();
        userService=UserService.getInstance();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String username= String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        List<User> userList=followService.followInfo(user);
        String res=userService.UserToJson(userList);
        out.write(res);
        out.close();
    }
}
