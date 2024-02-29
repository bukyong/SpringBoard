<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../include/header.jsp"%>

<h1>/board/list.jsp</h1>

글 개수 : ${boardList.size() }
viewUpdateStatus : ${viewUpdateStatus }

<div class="box">
	<div class="box-header with-border">
		<h3 class="box-title">게시판 목록</h3>
	</div>

	<div class="box-body">
		<table class="table table-bordered">
			<tbody>
				<tr>
					<th style="width: 20px">bno</th>
					<th>title(제목)</th>
					<th>writer(작성자)</th>
					<th style="width: 120px">viewcnt(조회수)</th>
					<th>regdate(날짜)</th>
				</tr>
				<c:forEach var="bVO" items="${boardList }">
					<tr>
						<td>${bVO.bno }</td>
						<td>
							<a href="/board/read?bno=${bVO.bno }">${bVO.title }</a>
						</td>
						<td>
							${bVO.writer }
						</td>
						<td><span class="badge bg-red">${bVO.viewcnt }</span></td>
						<td>
						   <fmt:formatDate value="${bVO.regdate }" pattern="yy.MM.dd"/> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="box-footer clearfix">
		<ul class="pagination pagination-sm no-margin pull-right">
			<li><a href="#">«</a></li>
			<li><a href="#">1</a></li>
			<li><a href="#">2</a></li>
			<li><a href="#">3</a></li>
			<li><a href="#">»</a></li>
		</ul>
	</div>
</div>

<%@ include file="../include/footer.jsp"%>