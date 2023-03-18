package com.lth.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "file_info")

public class FileInfoEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_seq") private Long seq;

	@Column(name = "file_uuid") private String filename;

	@Column(name = "file_original_name") private String originalName;

	@ManyToOne @JoinColumn(name = "file_board_seq") private BoardInfoEntity board;

}
