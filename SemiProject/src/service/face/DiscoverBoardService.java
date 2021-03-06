package service.face;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dto.DiscoverBoard;
import dto.DiscoverComment;
import dto.DiscoverImg;
import util.FindPaging;

public interface DiscoverBoardService {

	
	public List<DiscoverBoard> getList(FindPaging paging, Map<String, String> map);
	
	public FindPaging getPaging(HttpServletRequest req);

	public DiscoverBoard getParam(HttpServletRequest req);

	public DiscoverBoard views(DiscoverBoard discoverno);

	public Object getnick(DiscoverBoard viewDiscoverBoard);

	public Object getemail(DiscoverBoard viewDiscoverBoard);

	public List<DiscoverImg> viewFile(DiscoverBoard viewDiscoverBoard);

	public void write(HttpServletRequest req);

	public void update(HttpServletRequest req);

	public void delete(DiscoverBoard discoverboard);

	public List<DiscoverComment> viewComment(int discoverNo);

	public DiscoverComment getCommentParam(HttpServletRequest req);

	public void writeComment(DiscoverComment param);

	public void updateComment(DiscoverComment param);

	public void removeComment(DiscoverComment param);

	public void completeDiscover(int discoverno);
	
	
	
	
	


}
