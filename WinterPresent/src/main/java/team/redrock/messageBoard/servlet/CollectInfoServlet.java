package team.redrock.messageBoard.servlet;

import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.service.MessageBoardService;
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
import java.util.List;


public class CollectInfoServlet extends HttpServlet {

    MessageBoardService messageBoardService;
    LoginService loginService;

    @Override
    public void init() {
        loginService=LoginService.getInstance();
        messageBoardService = MessageBoardService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String username= String.valueOf(session.getAttribute("loginName"));
        User user=loginService.findUser(username);
        List<Message> messageList=messageBoardService.findCollection(user);
        String result = messageBoardService.messagesToJson(messageList);
        //返回收藏的微博相关信息
        out.write(result);
        out.close();
    }
}
