<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<br>
<div class="container" align="center">
	<div class="form-title">로그인</div>
	<div class="form-style">
		<form action="/auth/loginProc" method="POST">
			<div class="form-group" align="left">
				<label for="username">Username</label>
				<input type="text" name="username" class="form-control" placeholder="Enter username" id="username">
			</div>
			<div class="form-group" align="left">
				<div class="flex justify-content-between">
					<label for="password">Password</label>
					<a href="/auth/findForm">Forgot password?</a>
				</div>
				<input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
			</div>
			<div class="form-group form-check" align="left">
				<label class="form-check-label">
					<input class="form-check-input" name="remember" type="checkbox"> Remember me
				</label>
			</div>
			<span>
				<c:if test="${error}">
					<p id="valid" class="alert alert-danger">${exception}</p>
				</c:if>
			</span>
			<div align="right">
				<button id="btn-login" class="btn btn-login"><i class="fa-solid fa-lock"></i> 로그인</button>

		<!-- <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=796180152905-h4oikeegcrpsbi0it9svduifmbvq2315.apps.googleusercontent.com&redirect_uri=http://localhost:8000/auth/google/callback&response_type=code&scope=email%20profile">
           	<img height="38px" src="/image/gogle_login_btn.png" />
            </a>
        -->

			</div>
		</form>
	</div>
</div>
<br>

<%@ include file="../layout/footer.jsp"%>
