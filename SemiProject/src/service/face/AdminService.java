package service.face;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dto.Usertb;
import util.AdminPaging;
import util.Paging;

public interface AdminService {

	/**
	 * 모든 회원의 목록과 정보를 가져온다.
	 * @return	모든 회원의 정보
	 */
	List<Usertb> getList();

	/**
	 * 페이징 객체 생성
	 * @param req 페이지 정보를 담은 객체
	 * @return	페이징 정보를 가진 객체
	 */
	AdminPaging getPaging(HttpServletRequest req);

	/**
	 * 페이징 처리를 추가하여 회원의 목록을 가져온다.
	 * @param apaging	페이징 정보 객체
	 * @return		페이징 처리된 회원 목록
	 */
	List<Usertb> getList(AdminPaging apaging);


}
