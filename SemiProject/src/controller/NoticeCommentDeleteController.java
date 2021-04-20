package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.NoticeComment;
import service.face.NoticeService;
import service.impl.NoticeServiceImpl;

@WebServlet("/notice/commentdelete")
public class NoticeCommentDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private NoticeService noticeService = new NoticeServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		NoticeComment param = noticeService.getCommentParam(req);
		
		noticeService.removeComment(param); 
	
	
	}
}
