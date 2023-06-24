<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<div class="bg-dark" id="sidebar" align="center">
		<form id="userProfileImageForm">
			<input type="file" name="profileImageFile" id="userProfileImageInput" style="display: none;"/>
		</form>
		<c:choose>
			<c:when test="${not empty principal}">
				<img id="userProfileImage" src="/upload/${principal.user.profile_image_url}" onerror="this.src='/image/profile.jpg'" class="rounded-circle profile"
					onclick="profileImageUpload(${principal.user.id})" style="cursor: pointer;">
				<div class="font-bold">${principal.user.nickname}</div>
			</c:when>
			<c:otherwise>
				<img id="userProfileImage" src="/image/profile.jpg" class="rounded-circle profile">
				<div>비로그인 상태입니다.</div>
			</c:otherwise>
		</c:choose>

		<div>
        				<c:choose>
        					<c:when test="${empty principal}">
        						<ul class="navbar-nav">
        							<li class="nav-item"><a class="nav-link" href="/auth/loginForm"><i class="fa-solid fa-right-to-bracket"></i> 로그인 </a></li>
        							<li class="nav-item"><a class="nav-link" href="/auth/joinForm"><i class="fa-solid fa-user-plus"></i> 회원가입 </a></li>
        						</ul>
        					</c:when>
        					<c:otherwise>
        						<ul class="navbar-nav">
        							<li class="nav-item"><a class="nav-link" href="/user/updateForm"><i class="fa-solid fa-user"></i> 프로필 </a></li>
        							<li class="nav-item"><a class="nav-link" href="/logout"><i class="fa-solid fa-right-from-bracket"></i> 로그아웃</a></li>
        						</ul>
        					</c:otherwise>
        				</c:choose>
        			</div>
		<br>

	</div>

	<div class="overlay" onclick="sidebarHide()"></div>