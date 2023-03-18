package com.lth.community.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "member_info")
public class MemberInfoEntity implements UserDetails {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_seq") private Long seq;
  @Column(name = "member_id") private String memberId;
  @Column(name = "member_pw") private String pw;
  @Column(name = "member_name") private String name;
  @Column(name = "member_email") private String email;
  @Column(name = "member_nickname") private String nickname;
  @Column(name = "member_status") @ColumnDefault("1") private Integer status;
  @Column(name = "member_role") private String role;
  @Column(name = "member_refresh_token") private String refreshToken;
  @Column(name = "member_create_day") private LocalDateTime createDt;
  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<BoardInfoEntity> board = new ArrayList<>();
  @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
  private BanMemberInfoEntity banMember;
  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<CommentInfoEntity> comments = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> roles = new HashSet<>();
    roles.add(new SimpleGrantedAuthority(this.role));
    return roles;
  }

  @Override
  public String getPassword() {
    return this.pw;
  }

  @Override
  public String getUsername() {
    return this.memberId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() {
    return this.status == 1;
  }
}
