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
@Table(name = "delete_member_info")
public class DeleteMemberInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delete_seq") private Long seq;
    @Column(name = "delete_day") private LocalDate deleteDt;
    @OneToOne @JoinColumn(name = "delete_member_seq") MemberInfoEntity member;
}
