package service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import common.JDBCTemplate;
import dao.face.ProFileDao;
import dao.impl.ProFileDaoImpl;
import dto.UserAddress;
import dto.UserImg;
import dto.UserLeave;
import dto.Usertb;
import service.face.ProFileService;

public class ProFileServiceImpl implements ProFileService {

	ProFileDao proFileDao = new ProFileDaoImpl();

	@Override
	public void upDatebynickbyImg(HttpServletRequest req) {
		Usertb usertb = null;
		UserImg userimg = null;

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);

		if(!isMultipart) {

			usertb = new Usertb();
			usertb.setUserId( (String)req.getSession().getAttribute("userid") );
		} else {
			//파일 업로드를 사용 하고있을 경우
			usertb = new Usertb();

			//디스크팩토리
			DiskFileItemFactory factory = new DiskFileItemFactory();

			//메모리처리 사이즈
			factory.setSizeThreshold(1*1024*1024);// 1MB

			//컨텍스트 생성
			ServletContext context = req.getServletContext();

			//임시저장소
			File repository = new File( context.getRealPath("tmp") );
			repository.mkdir();

			factory.setRepository(repository);

			//업로드 객체 생성
			ServletFileUpload upload = new ServletFileUpload(factory);

			//용량 제한 설정:10MB
			upload.setFileSizeMax(10*1024*1024);

			//form-data 추출
			List<FileItem> items = null;

			try {
				items = upload.parseRequest(req);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			//파싱된 데이터 처리 반복자
			Iterator<FileItem> iter = items.iterator();

			//요청정보 처리
			while( iter.hasNext() ) {
				FileItem item = iter.next();

				//빈 파일 처리
				if(item.getSize() <=0 ) continue;

				//빈 파일이 아닌경우
				if(item.isFormField() ) {
					try {

						if("nick".equals(item.getFieldName() ) ) {
							usertb.setNick(item.getString("utf-8"));
						}

						usertb.setUserNo((int)req.getSession().getAttribute("userno"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {

					UUID uuid = UUID.randomUUID();
					String u = uuid.toString().split("-")[3];

					//로컬 저장소 파일
					String stored = u + "_" + item.getName();

					//파일 객체 생성
					File upFolder = new File(context.getRealPath("userimgup")); // 업로드될 폴더 경로
					upFolder.mkdir();

					File up = new File(upFolder, stored);


					userimg = new UserImg();
					userimg.setOriginName(item.getName());
					userimg.setStroedName(stored);
					userimg.setFilesize((int)item.getSize());


					try {
						//실제 업로드
						item.write(up);

						//임시 파일 삭제
						item.delete();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //try end
				} //if(item.isFormField() ) end
			}// while( iter.hasNext() ) end 


		}//if(!isMultipart) end

		//		System.out.println(usertb);
		//		System.out.println(userimg);

		Connection conn = JDBCTemplate.getConnection();

		if( usertb != null || !"".equals(usertb.getNick())) {
			if( proFileDao.updatebynick(conn, usertb) > 0 ) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}
		}

		if( userimg != null ) {
			userimg.setUserNo( (int)req.getSession().getAttribute("userno") );

			if( proFileDao.deletebyimg(conn, userimg) > 0) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}


			if( proFileDao.insertbyimg(conn, userimg) > 0 ) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}
		}
	}// public void upDatebynickbyImg(HttpServletRequest req) end


	@Override
	public UserImg getuserno(HttpServletRequest req) {
		UserImg userimg = new UserImg();

		userimg.setUserNo( (int) req.getSession().getAttribute("userno") );

		return userimg;
	}


	@Override
	public UserImg viewimg(UserImg userimg) {
		Connection conn = JDBCTemplate.getConnection();

		return proFileDao.selectUserimg(conn, userimg);
	}


	@Override
	public void basicimg(HttpServletRequest req) {

		Connection conn = JDBCTemplate.getConnection();

		UserImg userimg = new UserImg();

		userimg.setUserNo( (int)req.getSession().getAttribute("userno") );

		if( proFileDao.deletebyimg(conn, userimg) > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		if( proFileDao.basicinsert(conn, userimg) > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}


	}


	@Override
	public String getPw(HttpServletRequest req) {

		int userno = (int)req.getSession().getAttribute("userno");

		Connection conn = JDBCTemplate.getConnection();

		String Pw = proFileDao.selectPw(conn, userno);

		return Pw;
	}


	@Override
	public int getUserUpdate(HttpServletRequest req) {

		Connection conn = JDBCTemplate.getConnection();

		int res = 0;

		Usertb usertb = new Usertb();
		usertb.setUserNo( (int)req.getSession().getAttribute("userno") );
		if ( req.getParameter("userpw_up") != null && req.getParameter("userpw_up") != "" ) {

			usertb.setUserPw( req.getParameter("userpw_up") );

			res = proFileDao.updatebypw(conn, usertb);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}

		}

		res = 0;
		if ( req.getParameter("email") != null && req.getParameter("email") != "" ) {

			usertb.setEmail( req.getParameter("email") );

			res = proFileDao.updatebyemail(conn, usertb);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}

		res = 0;
		if ( req.getParameter("tel") != null && req.getParameter("tel") != "" ) {

			usertb.setPhone( req.getParameter("tel") );

			res = proFileDao.updatebyPhone(conn, usertb);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}

		return res;
	}


	@Override
	public int UserAddress(HttpServletRequest req) {

		Connection conn = JDBCTemplate.getConnection();

		UserAddress userAddress = new UserAddress();
		userAddress.setUserNo( (int)req.getSession().getAttribute("userno") );
		userAddress.setUserPostcode( req.getParameter("user_postcode"));
		userAddress.setUserRoadAddress( req.getParameter("user_roadAddress"));
		userAddress.setUserJibunAddress( req.getParameter("user_jibunAddress"));
		userAddress.setUserDetailAddress( req.getParameter("user_detailAddress"));
		userAddress.setUserExtraAddress( req.getParameter("user_extraAddress"));


		String postcode = (String) proFileDao.selectbyPost(conn, userAddress) ;
		//결과값 초기화
		int res = 0;

		//postcode 입력
		if( "".equals( postcode  ) ) {

			res = proFileDao.getinsertaddress(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}


		//결과값 초기화
		res = 0;

		//postcode update
		if ( !"".equals( req.getParameter("user_postcode") ) ) {

			res = proFileDao.updatepost(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}

		}

		res = 0;

		//roadAddress update
		if ( !"".equals( req.getParameter("user_roadAddress") ) ) {

			res = proFileDao.updateroad(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}

		}

		res = 0;
		//jibunAddress update
		if ( !"".equals( req.getParameter("user_jibunAddress") ) ) {

			res = proFileDao.updatejibun(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}


		res = 0;
		//detailAddress update
		if ( !"".equals( req.getParameter("user_detailAddress") ) ) {

			res = proFileDao.updatedetail(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}

		//extraAddress update
		if ( !"".equals( req.getParameter("user_extraAddress") ) ) {

			res = proFileDao.updateExtra(conn, userAddress);

			if ( res > 0 ) {

				JDBCTemplate.commit(conn);
			} else {

				JDBCTemplate.rollback(conn);
			}
		}

		return res;
	}


	@Override
	public int getuserleave(HttpServletRequest req) {

		Connection conn = JDBCTemplate.getConnection();

		int userno = (int) req.getSession().getAttribute("userno");

		int res = 0;

		res = proFileDao.deleteuser(conn, userno);

		if ( res > 0 ) {

			JDBCTemplate.commit(conn);
		} else {

			JDBCTemplate.rollback(conn);
		}

		res = 0;

		UserLeave userLeave = new UserLeave();
		userLeave.setContent( req.getParameter("content") );

		res = proFileDao.insertleave(conn, userLeave);

		if ( res > 0 ) {

			JDBCTemplate.commit(conn);
		} else {

			JDBCTemplate.rollback(conn);
		}

		return res;
	}

}
