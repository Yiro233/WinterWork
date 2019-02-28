package team.redrock.user.servlet;

import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.messageBoard.service.MessageBoardService;
import team.redrock.user.been.User;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.CollectService;
import team.redrock.user.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


public class CollectServlet extends HttpServlet {
    LoginService loginService;
    CollectService collectService;
    MessageBoardService messageBoardService;

    @Override
    public void init() {
        loginService = LoginService.getInstance();
        collectService=CollectService.getInstance();
        messageBoardService=MessageBoardService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String username= String.valueOf(session.getAttribute("loginName"));
        String collectIdStr = request.getParameter( "collect_id" );
        String loadIdStr=request.getParameter("load_collect_id");
        int collect_id = Integer.parseInt(collectIdStr == null || "".equals(collectIdStr)?"0":collectIdStr);
        int load_collect_id = Integer.parseInt(loadIdStr == null || "".equals(loadIdStr)?"0":loadIdStr);

        if(collect_id!=0&&load_collect_id==0){
            User user=loginService.findUser(username);
            Message message=messageBoardService.findById(collect_id);
            String res=collectService.collect(user,message);
            out.write(res);
            out.close();
        }
        if(collect_id==0&&load_collect_id!=0){
            User user=loginService.findUser(username);
            Message message=messageBoardService.findById(load_collect_id);
            boolean flag=collectService.decide(user,message);
            String res="collect";
            if(flag){
                out.write(res);
                out.close();
            }else{
                res="cancel";
                out.write(res);
                out.close();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
