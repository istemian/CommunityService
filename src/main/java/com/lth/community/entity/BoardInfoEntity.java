package com.lth.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "board_info")
public class BoardInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq") private Long seq;
    @Column(name = "board_id") private String boardId;
    @Column(name = "board_pw") private String pw;
    @Column(name = "board_title") private String title;
    @Column(name = "board_content") private String content;
    @Column(name = "board_create_day") private LocalDateTime creatDt;
    @Column(name = "board_modified_day") private LocalDateTime modifiedDt;
    @ManyToOne @JoinColumn(name = "board_member_seq") private MemberInfoEntity member;
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<FileInfoEntity> files = new ArrayList<>();
}
