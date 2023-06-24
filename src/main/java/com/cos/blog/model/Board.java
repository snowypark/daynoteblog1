package com.cos.blog.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "board") // 테이블 이름 명시
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Lob // 대용량 데이터를 저장하기 위함
	private String content;

	private int count;

	@JsonIgnoreProperties({ "boards", "replys" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnoreProperties({ "board", "user" })
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	@OrderBy("id desc")
	private List<Reply> replys = new ArrayList<>();

	@JsonIgnoreProperties({ "board", "user" })
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Recommend> recommends;

	private int recommendCount;

	private String userNickname;

	private String seen;

	private String category;

	@Transient
	private Board prev_board;

	@Transient
	private Board next_board;

	@Transient
	private boolean recommend_state;

	@CreationTimestamp
	private Timestamp create_date;

	public String getCreateDate() {
		return new SimpleDateFormat("yyyy.MM.dd").format(create_date);
	}


}
