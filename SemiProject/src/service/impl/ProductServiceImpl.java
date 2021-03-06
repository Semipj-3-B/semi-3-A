package service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import common.JDBCTemplate;
import dao.face.ProductDao;
import dao.impl.ProductDaoImpl;
import dto.Coupon;
import dto.Product;
import dto.ProductImg;
import dto.Userorder;
import service.face.ProductService;
import util.ProductPaging;

public class ProductServiceImpl implements ProductService {

	private ProductDao productDao = new ProductDaoImpl();
	
	
	@Override
	public ProductPaging getPaping(HttpServletRequest req) {
		
		
		String param = req.getParameter("curPage");
		int curPage = 0;
		
		if( param != null && !"".equals(param) ) {
			curPage = Integer.parseInt(param);
		}
		
		int totalCount = productDao.selectCntAll(JDBCTemplate.getConnection() );
		
		ProductPaging paging = new ProductPaging(totalCount, curPage);
		
		return paging;
	}
	
	@Override
	public List<Product> getList(ProductPaging paging) {
		return productDao.selectAll(JDBCTemplate.getConnection(), paging);
	}
	
	@Override
	public List<Product> getList1(ProductPaging paging) {
		return productDao.selectAll1(JDBCTemplate.getConnection(), paging);
	}

	@Override
	public List<Product> getList2(ProductPaging paging) {
		return productDao.selectAll2(JDBCTemplate.getConnection(), paging);
	}

	@Override
	public List<Product> getList3(ProductPaging paging) {
		return productDao.selectAll3(JDBCTemplate.getConnection(), paging);
	}
	
	@Override
	public Product getProdByProdId(HttpServletRequest req) {
		
		//productId를 저장할 객체 생성
		Product productId = new Product();
		
		//전달된 ProductId 검증하기
		String param = req.getParameter("productId");
		
		System.out.println("productId: " + param);
		

		if(param != null && !"".equals(param)) {
			
			//파라미터 추출
			productId.setProductId( Integer.parseInt(param) );
		}
		
		//결과 반환
		return productId;
	}

	@Override
	public Product views(Product productid) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		//상품상세글 조회
		Product productview = productDao.selectProdByProdId(conn, productid);
		
		return productview;
	}
	
	/**
	 * 썸네일 구하기인데 모르겠다
	 */
	@Override
	public List<ProductImg> viewMainImg(List<Product> product) {
		
		return productDao.selectMainImg(JDBCTemplate.getConnection(), product);
	}


	@Override
	public List<ProductImg> viewImg(Product viewProduct) {
		
		return productDao.selectImg(JDBCTemplate.getConnection(), viewProduct);
	}

	@Override
	public void write(HttpServletRequest req) {
		
		Product product = null;
		ProductImg productImg = null;
		
		List<ProductImg> productImgs = new ArrayList<ProductImg>();
		
		//파일업로드 형태의 데이터가 맞는지 검사
		boolean isMutltipart = false;
		isMutltipart = ServletFileUpload.isMultipartContent(req);
		
		if(!isMutltipart) {
			System.err.println("multipart/form-data 형식이 아닙니다.");
			return;
		}
		
		//multipart/form-data일 때 인스턴스 생성
		product = new Product();
		
		Connection conn = JDBCTemplate.getConnection();
		int productId = productDao.selectproductId(conn);
		product.setProductId(productId);
		
		//DiskFileItemFactory: FileItem 오브젝트 생성 및 메모리/HDD에서의 데이터 처리 기능을 가진다.
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		//메모리 처리 사이즈 지정
		final int MEM_SIZE = 1 * 1024; 	//1KB
		factory.setSizeThreshold(MEM_SIZE);	
		
		//임시 저장소(name: tmp2) 설정
		File repository = new File(req.getServletContext().getRealPath("tmp2"));
		repository.mkdir();
		
		factory.setRepository(repository);
		
		//파일업로드 객체 생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//업로드 용량 제한
		final int MAX_SIZE = 10 * 1024 * 1024;	//10MB
		upload.setFileSizeMax(MAX_SIZE);	
		
		//전달 데이터 파싱
		List<FileItem> items = null;
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		//추출된 전달파라미터 처리 반복자
		Iterator<FileItem> iter = items.iterator();
		
		//모든 요청 정보 처리하기
		while(iter.hasNext()) {
			FileItem item = iter.next();
			
			//빈 파일 처리
			if(item.getSize() <= 0)	continue;
			
			//일반적인 요청 데이터 처리
			if(item.isFormField()) {
				//name 값으로 키 추출
				String key = item.getFieldName();

				//NOT NULL 파라미터 먼저 처리 (NOT NULL: title, petkinds, loc)
				if(key != null && !"".equals(key)) {
					
					//전달 파라미터 name이 "sort"
					if("sort".equals(key)) {
						 try {
							product.setCategoryId(Integer.parseInt(item.getString("UTF-8")));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} //if(sort .key) END
					
					if("prodname".equals(key)) {
						try {
							product.setProductName(item.getString("UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} //if(prodname.key) END
					
					if("price".equals(key)) {
						try {
							product.setPrice(Integer.parseInt(item.getString("UTF-8")));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} //if(price.key) END
				} //if(NOT NULL) END
				
				//NULL 허용 데이터 처리
				if("content".equals(key) ) {
					try {
						product.setContent(item.getString("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			} //if(isFormField) END
			
			//파일 처리
			if(!item.isFormField()) {
				//확장자 추출
				int lastDot = item.getName().lastIndexOf('.');
				String ext = item.getName().substring(lastDot + 1);

				//확장자 유효 검사
				boolean isImg = false;
				if("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext)) isImg = true;
				
				//파일명 유효 검사
				boolean isValidName = false;
				String originName = item.getName().substring(0, lastDot);
				
				//파일명 String -> Byte 길이로 변환
				int nameToBytes = 0;
				try {
					nameToBytes = originName.getBytes("UTF-8").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				//30byte 초과시 false
				final int maxBytes = 30;
				if(nameToBytes <= maxBytes) isValidName = true;
				else {
					originName = originName.substring(0, 9);
					isValidName = true;
				}
				
				//확장자, 파일명 모두 유효할 때만 파일 저장 및 DB 삽입
				if(isImg && isValidName) {
					
					//UUID 생성
					UUID uuid = UUID.randomUUID();
					String uid = uuid.toString().split("-")[0];
					
					//파일이 저장될 이름을 설정(originName_xxxxxxxx)
					String storedName = originName + "_" + uid;
					
					//로컬 저장소에 파일 객체(upload 폴더) 생성
					File uploFolder = new File(req.getServletContext().getRealPath("uploadProd"));
					uploFolder.mkdir();
					
					File upFile = new File(uploFolder, storedName);
					
					productImg = new ProductImg();
					productImg.setProductId(productId);
					productImg.setOriginImg(originName);
					productImg.setStoredImg(storedName);
					
					productImgs.add(productImg);
					
					//처리가 완료된 파일 업로드
					try {
						item.write(upFile);	//실제 업로드
						item.delete();		//임시 파일 삭제
					} catch (Exception e) {
						e.printStackTrace();
					}
				} //if(isImg) END
				
			} //if(!ifFormField) END

		} //while(iter.hasnext) END
		
		System.out.println("======Product 테스트======");
		System.out.println("상품ID: " + product.getProductId());
		System.out.println("카테고리ID: " + product.getCategoryId());
		System.out.println("상품명: " + product.getProductName());
		System.out.println("가격: " + product.getPrice());
		System.out.println("상세내용: " + product.getContent());
		System.out.println("========================");
		System.out.println();
		System.out.println("=====상품 이미지 테스트 1=====");
		System.out.println("상품ID: " +productImgs.get(0).getProductId());
		System.out.println("원본이름: " + productImgs.get(0).getOriginImg());
		System.out.println("저장이름: " + productImgs.get(0).getStoredImg());
		System.out.println("========================");
		System.out.println();
		System.out.println("=====상품 이미지 테스트 2=====");
		System.out.println("상품ID: " +productImgs.get(1).getProductId());
		System.out.println("원본이름: " + productImgs.get(1).getOriginImg());
		System.out.println("저장이름: " + productImgs.get(1).getStoredImg());
		System.out.println("========================");
		
		System.out.println("product의 존재 유뮤 = " + product);
		
		if(product != null) {
			if(productDao.insert(conn, product) > 0) {
				System.out.println("서비스 부분 = insert로 넘어가냐");
				JDBCTemplate.commit(conn);
			} else {
				System.out.println("서비스 부분 = insert로 못 넘어가냐");
				JDBCTemplate.rollback(conn);
			}
		}
		
		if(productImgs != null) {
			if(productDao.insertImg(conn, productImgs) > 0) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}
		}
		
	}

	@Override
	public Userorder getOrderParam(HttpServletRequest req) {
		
		Userorder userorder = new Userorder();
		
		//얻어온 상품 아이디 검사
		String param1 = req.getParameter("productId");
		
		int productId = 0;
		if( param1 != null && !"".equals(param1)) {
			productId = Integer.parseInt(param1);
		}
		
		System.out.println("product service의 productId 추출값 = " + productId);
		
		//얻어온 유저 번호 검사
		String param2 = req.getParameter("userNo");
		
		int userNo = 0;
		if( param2 != null && !"".equals(param2)) {
			userNo = Integer.parseInt(param2);
		}
		
		System.out.println("product service의 userNo 추출값 = " + userNo);
		
		//얻어온 유저 번호 검사
		String param3 = req.getParameter("totalPay");
				
		int totalPay = 0;
		if( param3 != null && !"".equals(param3)) {
			totalPay = Integer.parseInt(param3);
		}
				
		System.out.println("product service의 totalPay 추출값 = " + totalPay);
				
		
		userorder.setProduct_id(productId);
		userorder.setUser_no(userNo);
		userorder.setTotal_pay(totalPay);
		
		
		
		return userorder;
	}

	@Override
	public void insertOrder(Userorder param) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int res = productDao.insertUserOrder(conn, param);
		
		if(res > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);				
		}
		
	}
	
	@Override
	public List<Coupon> lookUpCoupon(int userno) {

		Connection conn = JDBCTemplate.getConnection();
		
		List<Coupon> coupon = productDao.selectCoupon(conn, userno);
		
		return coupon;
	}








}
