package team.redrock.user.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;


public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        session.setAttribute("loginName", null);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(URLDecoder.decode(cookie.getName(), "utf-8"));
                if (URLDecoder.decode(cookie.getName(), "utf-8").equals("username")) {
                    cookie.setMaxAge(0);//将username的cookie设置成0秒
                    response.addCookie(cookie);
                }
            }
        }
        response.sendRedirect(request.getContextPath()+"/weibo.html");
    }
}
