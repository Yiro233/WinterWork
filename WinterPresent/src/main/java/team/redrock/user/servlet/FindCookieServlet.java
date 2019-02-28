package team.redrock.user.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;


public class FindCookieServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        boolean flag = false;
        PrintWriter out = response.getWriter();

        // 判断cookie是否有username，如果有代表登陆过
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(URLDecoder.decode(cookie.getName(), "utf-8"));
                if (URLDecoder.decode(cookie.getName(), "utf-8").equals("username")) { // 表明已经登陆过了，就直接跳转了
                    flag = true;
                }
            }
        }

        if(flag) {
            response.sendRedirect(request.getContextPath()+"/mainpage.html");

        }else {
            out.write("<html>"
                    + "<head><script type='text/javascript'> alert('没有登陆过，请登录!');location='login.html';</script></head>"
                    + "<body></body></html>");
        }
        out.close();
    }
}
