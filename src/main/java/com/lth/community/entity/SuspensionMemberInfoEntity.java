package com.lth.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "suspension_member_info")
public class SuspensionMemberInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suspension_seq") private Long seq;
    @Column(name = "suspension_start_day") private LocalDate startDt;
    @Column(name = "suspension_end_day") private LocalDate endDt;
    @Column(name = "suspension_reason") private String reason;
    @OneToOne @JoinColumn(name = "suspension_member_seq") private MemberInfoEntity member;

}
