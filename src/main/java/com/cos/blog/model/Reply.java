package com.cos.blog.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reply") // 테이블 이름 명시
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String content;

	@JsonIgnoreProperties({"user", "replys"})
	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;

	@JsonIgnoreProperties({"boards", "replys"})
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private boolean alarm_confirm_state;

	@CreationTimestamp
	private Timestamp create_date;

	public String getCreateDate() {
		return new SimpleDateFormat("yyyy.MM.dd").format(create_date);
	}

	public String getCreateDateTime() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(create_date);
	}
}
