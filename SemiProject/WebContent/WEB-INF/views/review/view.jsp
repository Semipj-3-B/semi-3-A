<%@page import="dto.ReviewImgFile"%>
<%@page import="java.util.List"%>
<%@page import="dto.ReviewBoard"%>
<%@page import="dto.ReviewDetailView"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% ReviewDetailView view = (ReviewDetailView) request.getAttribute("reviewTextView"); %>
<% List<ReviewImgFile> imgs = (List<ReviewImgFile>) request.getAttribute("reviewImgs"); %>
<% ReviewBoard reviewNo = (ReviewBoard) request.getAttribute("reviewNo"); %>

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<% String nick = (String) session.getAttribute("nick"); %>
<script type="text/javascript">
$(document).ready(function() {
	
	$("#btnList").click(function() {
		location.href="/review/list";
	});
	
	$("#btnUpdate").click(function() {
		$(location).attr("href", "/review/update?reviewNo=<%=reviewNo.getReviewNo() %>")
	});
	
	$("#btnDelete").click(function() {
		if(confirm("게시글을 삭제하시겠습니까?")) {
			$(location).attr("href", "/review/delete?reviewNo=<%=reviewNo.getReviewNo() %>")
		}
	});
	
	
	/* 이미지 팝업 */
	var img = document.getElementsByClassName('click_img');

	for(var x=0; x<img.length; x++) {
		img.item(x).onclick = function() {
			window.open(this.src)
		};
	};
	
});
</script>


<style type="text/css">

#content {
/* 	width: 100%; */
	width: 98%;
}

.tables {
	width: 1100px;
	background-color: #FDF5E6;
	border-radius: 15px;
}

#imgs {
	width: 240px;
	height: 200px;
	float: left;
	margin: 5px;
}

#commentUpdatebtn {
	border : none;
	background : none;
	text-align: center;
	padding: 5px 5px;
	outline: none;
	color: #000;
	border-radius: 24px;
	transition: 0.25s;
	cursor: pointer;
	margin-top : 20px;
}

#comment {
width : 500px;
height : 70px;
}

.commentWrapBox {
	width: 1200px;
	margin : 0 auto;
	padding-left : 180px;
}

.commentWrapBox .commentwrap {
	margin-top : 20px;
}

.commentWrapBox .commentwrap .combtn {
	color: red;
	position : relative;
	left : 500px;
	border : none;
	background : none;
	text-align: center;
	padding: 5px 5px;
	outline: none;
	border-radius: 24px;
	transition: 0.25s;
	cursor: pointer;
}

.commentWrapBox .commentwrap .commentDate {
	position: relative;
	left : 100px;
	display : inline-block;
}

.commentWrapBox .commentwrap .commentNick {
	display : inline-block;
}

.commentWrapBox .commentwrap .commentImg {
	width: 30px;
	height : 30px;
	margin-right : 10px;
	display : inline-block;
}

.commentWrapBox .commentwrap .commentImg img {
	width : 100%;
	height : 100%;
	object-fit : cover;
	border-radius: 50%
}

.commentWrapBox .commentwrap .commentBtn {
	display : inline-block;
}

.commentWrapBox .commentwrap .commentText {
	margin-top : 5px;
}

.commentWrapBox .fold {
   display : none;
}

.commentWrapBox #foldbtn {
   width : 800px;
   border : none;
   margin-top : 20px;
}

.boxs {
	border: 1px solid white;
 	padding: 2px; 
	background-color: white;
	border-radius: 8px;
	margin: 8px;
}

.btns {
	margin-left: 45%;
	background-color: #EBAA5F;
	color: white;
	border-radius: 8px;
	padding: 3px 15px;
	border: 0;
	outline: 0;
}

.center {
	text-align: center;
}

</style>


<div class="container">

<h3 style="text-align: center;">게시글 상세보기</h3>
<hr>

<!-- <table class="table table-bordered"> -->
<table class="tables">

<tr>
	<td class="center">분류</td>
	<td colspan="3"><div class="boxs"><%=view.getReviewSortDetail() %></div></td>
</tr>

<tr>
	<td class="center">제목</td>
	<td colspan="6"><div class="boxs"><%=view.getTitle() %></div></td>
</tr>

<tr>
	<td class="center">작성자</td><td><div class="boxs"><%=view.getNick() %></div></td>
	<td class="center">작성일</td><td><div class="boxs"><%=view.getCreateDate() %></div></td>
	<td colspan="2"></td>
	<% if(nick.equals(view.getNick()))  {%>
	<td><button type="button" id="btnUpdate" class="btns">수정</button></td>
	<td><button type="button" id="btnDelete" class="btns" style="margin-left: 0;">삭제</button></td>
	<% }%>
	
</tr>

<% int i = imgs.size(); %>	
	<% if(i <= 0) { %>
		<tr>
			<!-- 이미지 띄워줄 자리 -->
			<td colspan="2"><img src=".." alt="img1" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img2" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img3" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img4" id="imgs" class="click_img" /></td>
		</tr>
	<% } else if(i < 2 && i > 0) { %>
		<tr>
			<!-- 이미지 띄워줄 자리 -->
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(0).getStoredImg() %>" alt="img1" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img2" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img3" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img4" id="imgs" class="click_img" /></td>
		</tr>
	<% } else if(i < 3 && i > 1)  {%>	
		<tr>
			<!-- 이미지 띄워줄 자리 -->
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(0).getStoredImg() %>" alt="img1" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(1).getStoredImg() %>" alt="img2" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img3" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img4" id="imgs" class="click_img" /></td>
		</tr>
	<% } else if(i < 4 && i > 2) { %>
		<tr>
			<!-- 이미지 띄워줄 자리 -->
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(0).getStoredImg() %>" alt="img1" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(1).getStoredImg() %>" alt="img2" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(2).getStoredImg() %>" alt="img3" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src=".." alt="img4" id="imgs" class="click_img" /></td>
		</tr>
	<% } else if(i < 5 && i > 3) { %>
		<tr>
			<!-- 이미지 띄워줄 자리 -->
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(0).getStoredImg() %>" alt="img1" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(1).getStoredImg() %>" alt="img2" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(2).getStoredImg() %>" alt="img3" id="imgs" class="click_img" /></td>
			<td colspan="2"><img src="/reviewImgFile/<%=imgs.get(3).getStoredImg() %>" alt="img4" id="imgs" class="click_img" /></td>
		</tr>
	<% } %>
		
		
<tr> <!-- 본문 -->
	<td colspan="8">
<%-- 		<textarea style="width: 97%; height: 300px;" name="content" id="content"><%=view.getContent() %></textarea> --%>
		<div style="width: 1100px; height: 300px; border: 1px solid; margin: 10px; border: 0; background-color: white;">
		<p><%=view.getContent() %></p>
		</div>
	</td>
</tr>

<tr>
	<td colspan="8" style="text-align: right; padding-right: 15px; padding-bottom: 5px;">
		<button type="button" id="btnList" class="btn">목록으로</button>
	</td>
</tr>

</table>

<input type="hidden" id="nick" value="<%=nick %>">
<input type="hidden" id="reviewNo" value="<%=reviewNo.getReviewNo() %>">
<input type="hidden" id="userno" value="<%=session.getAttribute("userno")  %>">

<div class="commentWrapBox">
	<h4>댓글(<span id="commentCnt"></span>)</h4>
	
	<input type="text" id="comment" name="comment" />
	<input type="button" id="commentUpdatebtn" value="댓글 등록" />
	
	<div id="commentwrap">
	
	</div>
</div>

</div>

<script type="text/javascript">
/* 댓글 목록 조회 */
$(document).ready(function() {
	commentlist();
	
	$(document).on('click', '#foldbtn', function() {
		$('.fold').css('display', 'block');
		$('#foldbtn').css('display', 'none');
	})
});



/* 댓글 추가 */
$("#commentUpdatebtn").click(function() {
	
	var reviewNo = $("#reviewNo").val();
	var comment = $("#comment").val();
	var nick = $("#nick").val();
	var userno = $("#userno").val();
	
	$.ajax({
		type : 'post',
		url : '/review/commentinsert',
		data : {'reviewNo' : reviewNo,
				'comment' : comment,
				'nick' : nick,
				'userno' : userno},
		success : function() {
			$("#comment").val("");
			$("#commentwrap").html("");
			
			commentlist();
		}
	})
	
});



/* 댓글 목록 */
function commentlist() {
	var reviewNo = $("#reviewNo").val();
	
	$.ajax({
		type : 'get',
		url : '/review/commentList',
		data : {'reviewNo' : reviewNo},
		dataType : 'json',
		success : function(data) {
			var jsonText = JSON.stringify(data);
			var commentlist = JSON.parse(jsonText);
			
			for(var i=0; i<commentlist.length; i++) {
				
				var html = '';
				
				if(commentlist[i].commentCnt != null) {
					var commentCnt = commentlist[i].commentCnt;
				}
				
				if(i > 4) {
					html += "<div class='fold'>";
				}
				html += "<div class='commentwrap'>"
				html += "<div class='commentImg'><img src='"
				if(commentlist[i].storedName == "basic.png" || commentlist[i].storedName == null) {
					html += "/resources/image/basic.png"
				} else {
					html += "/userimgup/" + commentlist[i].storedName
				}
				html += " '/></div>"
				html += "<div class='commentNick'>" + commentlist[i].nick + "</div>"
				html += "<div class = 'commentDate'>" + commentlist[i].commentDate + "</div>"
				
				if($("#userno").val() == commentlist[i].userNo) {
					html += "<div class='commentBtn'>"
					html += "<input type='button' class='combtn' onclick = 'commentUpdateTrans(" + commentlist[i].commentNo + ")' id='updatebtn" + commentlist[i].commentNo + "' value='수정' />"
					html += "<input type='button' class='combtn' onclick = 'commentDelete(" + commentlist[i].commentNo + ")' id='deletebtn" + commentlist[i].commentNo + "' value='삭제' />"
					html += "</div><br>"
				}
				html += "<div class='commentText' id='comwrap" + commentlist[i].commentNo + "'>" + commentlist[i].commentText + "</div>"
				html += "</div>"
				
				if(i == commentlist.length-1) {
					html += "</div>"
				}
				if(i == 4) {
					html += "<input type='button' id='foldbtn' value='댓글 더보기'>"
				}
				
				$("#commentwrap").append(html);
			}
			$("#commentCnt").html("");
			$("#commentCnt").append(commentCnt);
		}
	})
}


/* 댓글 삭제 */
function commentDelete(commentno) {
	
	$.ajax({
		type: 'post',
		url : '/review/commentdelete',
		data : {'commentno' : commentno},
		success : function() {
			
			$("#commentwrap").html("");
			
			commentlist();
		}
	})
	
}



/* 댓글 수정 */
function commentUpdateTrans(commentno) {
	
	var contentText = $('#commentText' + commentno).text();
	
	$('#updatebtn' + commentno).css('display', 'none');
	$('#deletebtn' + commentno).css('display', 'none');
	
	var html = '';
	
	html += "<input type='text' id='comupdate' value='" + contentText + "' style='width: 300px; height: 40px;' />";
	html += "<input type='button' class='comupbtn' id='updateConfirm' onclick='commentUpdate(" + commentno + ")' value='수정' />";
	html += "<input type='button' class='comupbtn' id='canclebtn' onclick='updatecancel()' value='취소' />";
	
	$('#comwrap' + commentno).html(html)
	
}



/* 댓글 수정 - 수정버튼 클릭 */
function commentUpdate(commentno) {
	
	var comment = $('#comupdate').val();
	
	$.ajax({
		type: 'post',
		url : '/review/commentupdate',
		data : {'commentno' : commentno,
				'comment' : comment},
		success : function() {
			$("#commentwrap").html("");
			
			commentlist();
		}
	})
	
}


/* 댓글 수정 - 취소버튼 클릭 */
function updatecancel() {
	
	$("#commentwrap").html("");
	
	commentlist();
	
}

</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>