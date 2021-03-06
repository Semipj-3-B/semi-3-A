package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dto.Product;
import service.face.AdminService;
import service.impl.AdminServiceImpl;
import util.AdminPaging;

@WebServlet("/admin/product")
public class AdminProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	AdminService adminService = new AdminServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		List<Product> productList = null;
		
		//ajax로 요청 들어왔을 때
		String param = req.getParameter("categoryId");
		if(param != null && !"".equals(param)) {
			Gson gson = new Gson();
			int categoryId = Integer.parseInt(param);
			productList = adminService.getProdListByCateId(categoryId);
			
			resp.setContentType("application/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print(gson.toJson(productList));
			
		} else {
			AdminPaging apaging = adminService.getPaging(req);
			productList = adminService.getProductList(apaging);

			req.setAttribute("apaging", apaging);
			req.setAttribute("productList", productList);
			req.getRequestDispatcher("/WEB-INF/views/admin/product.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		adminService.deleteProduct(req);
		resp.sendRedirect("/admin/product");
	}
}
