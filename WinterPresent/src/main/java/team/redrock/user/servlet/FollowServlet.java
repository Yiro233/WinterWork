package team.redrock.user.servlet;



import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.FollowService;
import team.redrock.user.service.LoginService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


public class FollowServlet extends HttpServlet {

    LoginService loginService;
    FollowService followService;
    @Override
    public void init() {
        loginService = LoginService.getInstance();
        followService=FollowService.getInstance();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        UserDao userDao = new UserDao();
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String username = String.valueOf(session.getAttribute("loginName"));
        String follow_name = request.getParameter("follow_name");
        String load_follow_name = request.getParameter("load_follow_name");
        //如果是关注操作而非读取状态 则调用点赞函数
        if ((follow_name != null&&follow_name.length()!=0) && (load_follow_name == null||load_follow_name.length()==0)) {
            User user = loginService.findUser(username);
            User follow_user = loginService.findUserByNickName(follow_name);
            String res = followService.follow(user, follow_user);
            out.write(res);
            out.close();
        }
        //如果是读取状态而非关注 则返还当前状态
        if ((follow_name == null||follow_name.length()==0) && (load_follow_name != null&&load_follow_name.length()!=0)) {
            User user = loginService.findUser(username);
            User follow_user=loginService.findUserByNickName(load_follow_name);
            load_follow_name=follow_user.getName();
            boolean flag = followService.judge(user.getName(), load_follow_name);
            String res = "follow";
            if (flag) {
                out.write(res);
                out.close();
            } else {
                res = "cancel";
                out.write(res);
                out.close();
            }

        }

    }
}
