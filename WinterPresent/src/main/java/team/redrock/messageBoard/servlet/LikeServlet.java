package team.redrock.messageBoard.servlet;

import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.messageBoard.service.MessageBoardService;
import team.redrock.user.dao.UserDao;
import team.redrock.user.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


public class LikeServlet extends HttpServlet {
    MessageBoardService messageBoardService;

    @Override
    public void init() {
        messageBoardService = MessageBoardService.getInstance();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        HttpSession session = request.getSession();
        PrintWriter out=response.getWriter();
        String username= String.valueOf(session.getAttribute("loginName"));
        String likeidStr = request.getParameter( "likeid" );
        String loadidStr=request.getParameter("loadid");
        int likeid = Integer.parseInt(likeidStr == null || "".equals(likeidStr)?"0":likeidStr);
        int loadid = Integer.parseInt(loadidStr == null || "".equals(loadidStr)?"0":loadidStr);

        //如果是点赞操作而非读取状态 则调用点赞函数
        if(likeid!=0&&loadid==0){
            Message message= messageBoardService.findById(likeid);
            String res=messageBoardService.like(message,username);
            out.write(res);
            out.close();
        }
        //如果是读取状态而非点赞 则返还当前状态
        if(likeid==0&&loadid!=0){
            Message message= messageBoardService.findById(loadid);
            boolean flag=messageBoardService.judge(message,username);
            String res="like";
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
