package team.redrock.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


public class EncodingFilter implements Filter {
    private String charSet;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(this.charSet);
    }

    public void init(FilterConfig config) throws ServletException {
        this.charSet=config.getInitParameter("charSet");
    }

}
