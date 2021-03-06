package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.JDBCTemplate;
import dao.face.AdminDao;
import dto.DiscoverBoard;
import dto.FindBoard;
import dto.Product;
import dto.ReviewUserJoin;
import dto.Usertb;
import util.AdminPaging;

public class AdminDaoImpl implements AdminDao {

	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	@Override
	public List<Usertb> selectAll(Connection conn) {
		String sql = "";
		sql += "SELECT user_no, user_id, nick, email FROM usertb";
		sql += " ORDER BY user_no";
		
		List<Usertb> userList = new ArrayList<Usertb>();
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Usertb user = new Usertb();
				user.setUserNo(rs.getInt("user_no"));
				user.setUserId(rs.getString("user_id"));
				user.setNick(rs.getString("nick"));
				user.setEmail(rs.getString("email"));
				
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return userList;
	}

	@Override
	public int selectCntAll(Connection conn) {
		String sql = "";
		sql += "SELECT count(*) cnt FROM board";
		
		int cnt = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return cnt;
	}

	@Override
	public List<Usertb> selectAll(Connection conn, AdminPaging apaging) {
		//SQL 작성
		String sql = "";
		sql += "SELECT * FROM (";
		sql += " 	SELECT rownum rnum, U.* FROM (";
		sql += " 		SELECT user_no, user_id, nick, email FROM usertb";
		sql += " 		ORDER BY user_no ";
		sql += "	) U";
		sql += " ) ";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		List<Usertb> userList = new ArrayList<>();
		
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, apaging.getStartNo());
			ps.setInt(2, apaging.getEndNo());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Usertb user = new Usertb(); 
				
				user.setUserNo(rs.getInt("user_no"));
				user.setUserId(rs.getString("user_id"));
				user.setNick(rs.getString("nick"));
				user.setEmail(rs.getString("email"));
				
				userList.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return userList;
	}

	@Override
	public int deleteUserByUserno(Connection conn, int userno) {
		String sql = "";
		sql += "DELETE FROM usertb";
		sql += " WHERE user_no = ?";
		
		int result = -1;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userno);
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(ps);
		}
		
		return result;
	}

	@Override
	public List<FindBoard> selectFindBoard(Connection conn, AdminPaging apaging) {
		String sql = "";
		sql += "SELECT * FROM (";
		sql += " 	SELECT rownum rnum, F.* FROM (";
		sql += "		SELECT find_no, user_no, title, content, views FROM findboard";
		sql += " 		ORDER BY find_no DESC";
		sql += "	) F";
		sql += " ) ";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		List<FindBoard> findList = new ArrayList<FindBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, apaging.getStartNo());
			ps.setInt(2, apaging.getEndNo());
			rs = ps.executeQuery();
			
			while(rs.next()) {
				FindBoard findBoard = new FindBoard();
				
				findBoard.setFindNo(rs.getInt("find_no"));
				findBoard.setUserNo(rs.getInt("user_no"));
				findBoard.setTitle(rs.getString("title"));
				findBoard.setContent(rs.getString("content"));
				findBoard.setViews(rs.getInt("views"));
				
				findList.add(findBoard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return findList;
	}

	@Override
	public int deleteFindByFindno(Connection conn, int findno) {
		String sql = "";
		sql += "DELETE FROM findboard";
		sql += " WHERE find_no = ?";
		
		int result = -1;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, findno);
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(ps);
		}
		
		return result;
	}

	@Override
	public List<Product> selectProduct(Connection conn, AdminPaging apaging) {
		String sql = "";
		sql += "SELECT * FROM (";
		sql += "	SELECT rownum rnum, P.* FROM (";
		sql += "		SELECT product_id, category_id, product_name, price";
		sql += "		FROM product";
		sql += "		ORDER BY product_id";
		sql += "	) P";   
		sql += " ) WHERE rnum BETWEEN ? AND ?";   
			         
		List<Product> pList = new ArrayList<Product>();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, apaging.getStartNo());
			ps.setInt(2, apaging.getEndNo());
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Product product = new Product();
				
				product.setProductId(rs.getInt("product_id"));
				product.setCategoryId(rs.getInt("category_id"));
				product.setProductName(rs.getString("product_name"));
				product.setPrice(rs.getInt("price"));
				
				pList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return pList;
	}

	@Override
	public List<Product> selectProductByCateId(Connection conn, int categoryId) {
		String sql = "";
		sql += "SELECT product_id, product_name, price FROM product";
		sql += " WHERE category_id = ?";
		sql += " ORDER BY product_id";
			   
		List<Product> pList = new ArrayList<Product>();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, categoryId);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Product p = new Product();
				
				p.setProductId(rs.getInt("product_id"));
				p.setProductName(rs.getString("product_name"));
				p.setPrice(rs.getInt("price"));
				
				pList.add(p);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return pList;
	}

	@Override
	public int deleteProdByCateId(Connection conn, Product product) {
		String sql = "";
		sql += "DELETE FROM product";
		sql += " WHERE product_id = ?";
		
		int result = -1;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, product.getProductId());
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(ps);
		}
		return result;
	}

	@Override
	public List<FindBoard> selectFindByMap(Connection conn, Map<String, String> map) {
		String sql = "";
		sql += "SELECT find_no, user_no, title, content, views FROM findboard";
		sql += " WHERE pet_kinds = ? AND loc = ?";
		sql += " ORDER BY find_no DESC";
		
		List<FindBoard> fList = new ArrayList<FindBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, map.get("pet"));
			ps.setString(2, map.get("loc"));
			
			rs = ps.executeQuery();
			while(rs.next()) {
				FindBoard f = new FindBoard();
				
				f.setFindNo(rs.getInt("find_no"));
				f.setUserNo(rs.getInt("user_no"));
				f.setTitle(rs.getString("title"));
				f.setContent(rs.getString("content"));
				f.setViews(rs.getInt("views"));
				
				fList.add(f);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return fList;
	}

	@Override
	public List<FindBoard> selectFindByPet(Connection conn, String pet) {
		String sql = "";
		sql += "SELECT find_no, user_no, title, content, views FROM findboard";
		sql += " WHERE pet_kinds = ?";
		sql += " ORDER BY find_no DESC";
		
		List<FindBoard> fList = new ArrayList<FindBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, pet);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				FindBoard f = new FindBoard();
				
				f.setFindNo(rs.getInt("find_no"));
				f.setUserNo(rs.getInt("user_no"));
				f.setTitle(rs.getString("title"));
				f.setContent(rs.getString("content"));
				f.setViews(rs.getInt("views"));
				
				fList.add(f);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return fList;
	}

	@Override
	public List<FindBoard> selectFindByLoc(Connection conn, String loc) {
		String sql = "";
		sql += "SELECT find_no, user_no, title, content, views FROM findboard";
		sql += " WHERE loc = ?";
		sql += " ORDER BY find_no DESC";
		List<FindBoard> fList = new ArrayList<FindBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, loc);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				FindBoard f = new FindBoard();
				
				f.setFindNo(rs.getInt("find_no"));
				f.setUserNo(rs.getInt("user_no"));
				f.setTitle(rs.getString("title"));
				f.setContent(rs.getString("content"));
				f.setViews(rs.getInt("views"));
				
				fList.add(f);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return fList;
	}

	@Override
	public List<DiscoverBoard> selectDiscoverBoard(Connection conn, AdminPaging apaging) {
		String sql = "";
		sql += "SELECT * FROM (";
		sql += " 	SELECT rownum rnum, D.* FROM (";
		sql += "		SELECT discover_no, user_no, title, content, views FROM discoverboard";
		sql += " 		ORDER BY discover_no DESC";
		sql += "	) D";
		sql += " ) ";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		List<DiscoverBoard> discList = new ArrayList<DiscoverBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, apaging.getStartNo());
			ps.setInt(2, apaging.getEndNo());
			rs = ps.executeQuery();
			
			while(rs.next()) {
				DiscoverBoard d = new DiscoverBoard();
				
				d.setDiscoverNo(rs.getInt("discover_no"));
				d.setUserNo(rs.getInt("user_no"));
				d.setTitle(rs.getString("title"));
				d.setContent(rs.getString("content"));
				d.setViews(rs.getInt("views"));
				
				discList.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return discList;
	}

	@Override
	public List<DiscoverBoard> selectDiscByMap(Connection conn, Map<String, String> map) {
		String sql = "";
		sql += "SELECT discover_no, user_no, title, content, views FROM discoverboard";
		sql += " WHERE pet_kinds = ? AND loc = ?";
		sql += " ORDER BY discover_no DESC";
		
		List<DiscoverBoard> dList = new ArrayList<DiscoverBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, map.get("pet"));
			ps.setString(2, map.get("loc"));
			
			rs = ps.executeQuery();
			while(rs.next()) {
				DiscoverBoard d = new DiscoverBoard();
				
				d.setDiscoverNo(rs.getInt("discover_no"));
				d.setUserNo(rs.getInt("user_no"));
				d.setTitle(rs.getString("title"));
				d.setContent(rs.getString("content"));
				d.setViews(rs.getInt("views"));
				
				dList.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return dList;
	}

	@Override
	public List<DiscoverBoard> selectDiscByPet(Connection conn, String pet) {
		String sql = "";
		sql += "SELECT discover_no, user_no, title, content, views FROM discoverboard";
		sql += " WHERE pet_kinds = ?";
		sql += " ORDER BY discover_no DESC";
		
		List<DiscoverBoard> dList = new ArrayList<DiscoverBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, pet);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				DiscoverBoard d = new DiscoverBoard();
				
				d.setDiscoverNo(rs.getInt("discover_no"));
				d.setUserNo(rs.getInt("user_no"));
				d.setTitle(rs.getString("title"));
				d.setContent(rs.getString("content"));
				d.setViews(rs.getInt("views"));
				
				dList.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return dList;
	}

	@Override
	public List<DiscoverBoard> selectDiscByLoc(Connection conn, String loc) {
		String sql = "";
		sql += "SELECT discover_no, user_no, title, content, views FROM discoverboard";
		sql += " WHERE loc = ?";
		sql += " ORDER BY discover_no DESC";
		List<DiscoverBoard> dList = new ArrayList<DiscoverBoard>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, loc);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				DiscoverBoard d = new DiscoverBoard();
				
				d.setDiscoverNo(rs.getInt("discover_no"));
				d.setUserNo(rs.getInt("user_no"));
				d.setTitle(rs.getString("title"));
				d.setContent(rs.getString("content"));
				d.setViews(rs.getInt("views"));
				
				dList.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		return dList;
	}

	@Override
	public List<ReviewUserJoin> selectReviewBoard(Connection conn, AdminPaging apaging) {
		String sql = "";
		sql += " SELECT * FROM (";
		sql += "	    SELECT rownum rnum, R.* FROM (";
		sql += "	           SELECT";
		sql += "	               review_no, s.review_sort_detail, title, u.user_id, create_date, views";
		sql += "	           FROM review_board b, review_board_sort s, usertb u";
		sql += "	           WHERE b.review_sort = s.review_sort AND b.user_no = u.user_no";
		sql += "	           ORDER BY review_no DESC";
		sql += "	       ) R";
		sql += "	   ) REVIEW";
		sql += "	   WHERE rnum BETWEEN ? AND ?";
		
		//결과 저장
		List<ReviewUserJoin> reviewBoardList = new ArrayList<ReviewUserJoin>();
		
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, apaging.getStartNo());
			ps.setInt(2, apaging.getEndNo());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ReviewUserJoin review = new ReviewUserJoin(); // 결과값 저장 객체
				
				review.setReviewNo(rs.getInt("review_no"));
				review.setReviewSortDetail(rs.getString("review_sort_detail"));
				review.setTitle(rs.getString("title"));
				review.setUserId(rs.getString("user_id"));
				review.setCreateDate(rs.getDate("create_date"));
				review.setViews(rs.getInt("views"));
				
				reviewBoardList.add(review);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return reviewBoardList;
	}

	@Override
	public List<ReviewUserJoin> selectReviewBySort(Connection conn, int reviewSort) {
		String sql = "";
		sql += "SELECT";
		sql += "	review_no, s.review_sort_detail, title, u.user_id, create_date, views ";
		sql += "FROM ";
		sql += "	review_board b, review_board_sort s, usertb u ";
		sql += "WHERE b.review_sort = s.review_sort";
		sql += "		AND b.user_no = u.user_no";
		sql += "		AND s.review_sort = ? ";
		sql += "ORDER BY review_no DESC";
		
		//결과 저장
		List<ReviewUserJoin> reviewBoardList = new ArrayList<ReviewUserJoin>();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, reviewSort);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ReviewUserJoin review = new ReviewUserJoin(); // 결과값 저장 객체
				
				review.setReviewNo(rs.getInt("review_no"));
				review.setReviewSortDetail(rs.getString("review_sort_detail"));
				review.setTitle(rs.getString("title"));
				review.setUserId(rs.getString("user_id"));
				review.setCreateDate(rs.getDate("create_date"));
				review.setViews(rs.getInt("views"));
				
				reviewBoardList.add(review);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return reviewBoardList;
	}

	@Override
	public int deleteReviewByRevno(Connection conn, int reviewno) {
		String sql = "";
		sql += "DELETE FROM review_board";
		sql += " WHERE review_no = ?";
		
		int res = -1;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, reviewno);
			
			res = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(ps);
		}
				
		return res;
	}
}
