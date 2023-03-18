package com.lth.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "comment_info")
public class CommentInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq") private Long seq;
    @Column(name = "comment_nickname") private String nickname;
    @Column(name = "comment_pw") private String pw;
    @Column(name = "comment_content") private String content;
    @Column(name = "comment_creat_time") private LocalDateTime creatDt;
    @ManyToOne @JoinColumn(name = "comment_board_seq") private BoardInfoEntity board;
    @ManyToOne @JoinColumn(name = "comment_member_seq") private MemberInfoEntity member;

}
