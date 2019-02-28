package team.redrock.messageBoard.servlet;


import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.service.MessageBoardService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class InfoServlet extends HttpServlet {

    public static int num;
    MessageBoardService messageBoardService;

    @Override
    public void init() {
        messageBoardService = MessageBoardService.getInstance();
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String judge=req.getParameter("judge");
        PrintWriter out = resp.getWriter();
        List<Message> messageList=messageBoardService.findAllMessages();
        String res = messageBoardService.messagesToJson(messageList);

        if(judge!=null){
            if(judge.equals("load")){
                num=0;
                String partRes=messageBoardService.PartSend(res,num);
                num=num+6;
                out.write(partRes);
                out.close();
            }
            else if(judge.equals("scroll")){
                String partRes=messageBoardService.PartSend(res,num);
                num=num+6;
                out.write(partRes);
                out.close();
            }
        }else{
            out.write(res);
            out.close();
        }

    }
}
