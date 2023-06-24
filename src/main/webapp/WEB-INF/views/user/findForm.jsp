<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<br>
<div class="container" align="center">
	<div class="form-title">Reset your password</div>
	<div class="form-style">
		<form>
			<div align="left" style="word-spacing: -4px;">사용자 계정과 이메일 주소를 입력하면 비밀번호 재설정 링크를 보내드립니다.</div><br>
			<div class="form-group" align="left">
				<label for="username">Username</label> <input type="text" class="form-control" placeholder="Enter username" id="username">
			</div>
			<p class="valid-text" id="valid_username" align="left"></p>
			<div class="form-group" align="left">
				<label for="email">Email</label> <input type="email" class="form-control" placeholder="Enter email" id="email">
			</div>
			<p class="valid-text" id="valid_email" align="left"></p>
		</form>
		<div align="right">
			<button id="btn-find" class="btn btn-find"><i class="fa-solid fa-check"></i> Send password reset email</button>
		</div>
	</div>
</div>
<br>

<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>