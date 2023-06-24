package com.cos.blog.controller.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

	@Value("${spring.datasource.url}")
	private String url = "jdbc:mysql://daynoteblog-db.cnodvtkrz1fq.ap-northeast-2.rds.amazonaws.com:3306/daynoteblog?serverTimezone=Asia/Seoul";

	@Value("${spring.datasource.username}")
	private String username = "admin";

	@Value("${spring.datasource.password}")
	private String password = "note1234!";


	@PostMapping("/api/admin/data")
	public ResponseEntity<?> alarmConfirm() {

		Connection con = null;
		JSONObject responseObj = new JSONObject();

		try {
			// 드라이버 호출, 커넥션 연결
			con = DriverManager.getConnection(url, username, password);

			// DB에서 뽑아온 데이터(JSON)을 담을 객체. 후에 responseObj에 담기는 값
			List<JSONObject> boardList = new LinkedList<JSONObject>();

			String query = "select date(create_date) as 'cd', "
					+ "count(case when category = 'study' then 0 end) as 'study', "
					+ "count(case when category = 'diary' then 0 end) as 'diary' "
					+ "from board where create_date between date_add(now(), interval - 1 week) and now() group by cd "
					+ "order by cd";
			PreparedStatement pstm = con.prepareStatement(query);
			ResultSet rs = pstm.executeQuery(query);

			// ajax에 반환할 JSON 생성
			JSONObject lineObj = null;

			while (rs.next()) {
				String create_date = rs.getString("cd");
				int study = rs.getInt("study");
				int diary = rs.getInt("diary");

				lineObj = new JSONObject();
				lineObj.put("create_date", create_date);
				lineObj.put("study", study);
				lineObj.put("diary", diary);

				boardList.add(lineObj);
			}

			responseObj.put("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return new ResponseEntity<>(responseObj, HttpStatus.OK);
	}
}
