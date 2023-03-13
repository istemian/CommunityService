package com.lth.community.vo;

import com.lth.community.entity.MemberInfoEntity;
import lombok.Data;

@Data
public class MemberInfoVO {
  private String id;
  private String name;
  private String email;
  private String nickname;

  public MemberInfoVO(MemberInfoEntity entity) {
    this.id = entity.getMemberId();
    this.name = entity.getName();
    this.email = entity.getEmail();
    this.nickname = entity.getNickname();
  }
}
