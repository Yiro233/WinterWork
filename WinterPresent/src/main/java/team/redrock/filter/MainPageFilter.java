package team.redrock.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

public class MainPageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //不过滤的界面
        String[] notFilter=new String[]{"weibo.html"};

        String uri=req.getRequestURI();

        boolean doFilter=true;
        for(String s:notFilter){
            if(uri.indexOf(s)!=-1){
                //如果uri中包含不过滤的uri，则不进行过滤
                doFilter=false;
                break;
            }
        }
        if(doFilter){
            String username = String.valueOf(req.getSession().getAttribute("loginName"));
            if(username.equals("null")){
                req.setCharacterEncoding("UTF-8");
                res.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("username为空，非法操作，返回weibo.html");
                res.sendRedirect(req.getContextPath()+"/weibo.html");
            }else{
                filterChain.doFilter(req, res);
             }
            }else{
            filterChain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {

    }
}
