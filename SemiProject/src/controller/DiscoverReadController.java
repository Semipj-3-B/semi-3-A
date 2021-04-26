package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.DiscoverBoard;
import dto.DiscoverImg;
import service.face.DiscoverBoardService;
import service.impl.DiscoverBoardServiceImpl;


@WebServlet("/discover/read")
public class DiscoverReadController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DiscoverBoardService discoverBoardService = new DiscoverBoardServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		DiscoverBoard discoverno = discoverBoardService.getParam(req);
			
		System.out.println("discoverno = "+ discoverno);
		
		DiscoverBoard viewDiscoverBoard = discoverBoardService.views(discoverno);
		System.out.println("viewDiscoverBoard = "+ viewDiscoverBoard);
		
		req.setAttribute("nick", discoverBoardService.getnick(viewDiscoverBoard));
		req.setAttribute("email", discoverBoardService.getemail(viewDiscoverBoard));
		
//		System.out.println("/discover/read 의 emali = " + viewDiscoverBoard);
		req.setAttribute("viewDiscoverBoard", viewDiscoverBoard);
		
//		System.out.println("viewDiscoverBoard = "+ viewDiscoverBoard);
		
		List<DiscoverImg> discoverImg = discoverBoardService.viewFile(viewDiscoverBoard);
		req.setAttribute("discoverImg", discoverImg);
		
		System.out.println("discoverImg = "+ discoverImg);
		
		req.getRequestDispatcher("/WEB-INF/views/discover/read.jsp")
			.forward(req, resp);
		
		
	}
	

	
	
}
