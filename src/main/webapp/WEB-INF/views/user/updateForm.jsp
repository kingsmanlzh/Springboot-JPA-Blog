<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>
<div class="container">
	<form>
		<input type="hidden" id="id" value="${principal.user.id }" />


		<div>
			<label for="username">User name</label> <input type="text" value="${principal.user.username }" class="form-control" placeholder="Enter username" id="username" readonly>
		</div>

		<c:if test="${empty principal.user.oauth}">

			<div>
				<label for="pwd">Password</label> <input  autofocus type="password" class="form-control" placeholder="Enter password" id="password">
			</div>
			<div>
				<label for="email">Email</label> <input type="email" value="${ principal.user.email}" class="form-control" placeholder="Enter email" id="email">
			</div>

		</c:if>
		
		<c:if test="${not empty principal.user.oauth}">
             	<div>
				<label for="email">Email</label> <input type="email" value="${ principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
			    </div>
			    <br/><br/>
		</c:if>
	</form>
	<c:if test="${empty principal.user.oauth}">
	  <button id="btn-update" class="btn btn-primary" >회원수정완료</button>
	  <br/><br/>
	</c:if>  

</div>

<script src="/js/user.js"></script>

<%@ include file="../layout/footer.jsp"%>
