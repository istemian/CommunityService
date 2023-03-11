package com.lth.community.vo;

import com.lth.community.entity.MemberInfoEntity;
import lombok.Data;

@Data
public class MemberInfoVO {
  private Long no;
  private String id;
  private String name;
  private String email;
  private String nickname;
  private Integer status;

  public MemberInfoVO(MemberInfoEntity entity) {
    this.no = entity.getSeq();
    this.id = entity.getMemberId();
    this.name = entity.getName();
    this.email = entity.getEmail();
    this.nickname = entity.getNickname();
    this.status = entity.getStatus();
  }
}
