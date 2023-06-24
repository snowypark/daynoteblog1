<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div id="wrapper">
	<div id="content" class="container">
		<form>
			<select id="category" class="form-control">
				<option value="study">개발</option>
				<option value="diary">일기</option>
			</select><br>
			<input type="hidden" id="board_id" value="${board.id}">
			<div class="form-group">
				<label for="title"><i class="fa-solid fa-pen-nib"></i> 제목</label>
				<input value="${board.title}" type="text" class="form-control" placeholder="Enter title" id="title">
			</div>
			<div class="form-group">
				<label for="content"><i class="fa-solid fa-pen-nib"></i> 내용</label>
				<textarea class="form-control summernote" rows="5" id="content">${board.content}</textarea>
			</div>
			<!-- <div class="form-group">
				<label for="tags"><i class="fa-solid fa-pen-nib"></i> 태그</label>
				<input type="text" class="form-control" placeholder="태그를 입력하세요" id="tags">
			</div> -->
		</form>
		<button id="btn-update" class="btn btn-primary"><i class="fa-solid fa-check"></i> 완료</button>
	</div>
</div>

<script>
	$('.summernote').summernote({
		tabsize : 2,
		height : 300
	});

	// 완료 버튼 클릭 이벤트
	$("#btn-update").click(function() {
		var category = $("#category").val();
		var title = $("#title").val();
		var content = $("#content").val();
		var tags = $("#tags").val().split(",").map(function(tag) {
			return tag.trim();
		});

		var board = {
			category: category,
			title: title,
			content: content,
			tags: tags
		};

		$.ajax({
			type: "PUT",
			url: "/boards/" + $("#board_id").val(),
			data: JSON.stringify(board),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function(response) {
				// 수정 성공 시 처리
				// ...
				saveTags(tags); // 태그 저장 함수 호출
			},
			error: function(xhr, status, error) {
				// 수정 실패 시 처리
				// ...
			}
		});
	});

	function saveTags(tags) {
		// 태그 저장 처리
		// ...
	}
</script>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>
