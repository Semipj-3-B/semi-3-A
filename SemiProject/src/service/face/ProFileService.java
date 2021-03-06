package service.face;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dto.MyBoard;
import dto.PurchaseList;
import dto.UserImg;
import util.MyPaging;

public interface ProFileService {

	public boolean upDatebynickbyImg(HttpServletRequest req);

	public UserImg getuserno(HttpServletRequest req);

	public UserImg viewimg(UserImg userimg);

	public void basicimg(HttpServletRequest req);

	public boolean getPw(HttpServletRequest req);

	public int getUserUpdate(HttpServletRequest req);

	public int getuserleave(HttpServletRequest req);

	public List<MyBoard> myboradlist(HttpServletRequest req);

	public String getNick(HttpServletRequest req);

	public MyPaging getPageing(HttpServletRequest req);

	public List<MyBoard> myboradlist(HttpServletRequest req, MyPaging myPaging);

	public int deleteMyBoard(HttpServletRequest req);

	public int insertmypocket(HttpServletRequest req);


	public List<PurchaseList> myorderList(HttpServletRequest req);



	

}
