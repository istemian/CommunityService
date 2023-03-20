package com.lth.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "ban_member_info")
public class BanMemberInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_seq") private Long seq;
    @Column(name = "ban_start_day") private LocalDateTime startDt;
    @Column(name = "ban_end_day") private LocalDateTime endDt;
    @Column(name = "ban_reason") private String reason;
    @OneToOne @JoinColumn(name = "ban_member_seq") private MemberInfoEntity member;

}
