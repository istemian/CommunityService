package com.lth.community.repository;

import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfoEntity, Long> {
  public MemberInfoEntity findByMemberId(String memberId);
  public Long countByMemberId(String memberId);
  public Long countByNickname(String nickname);
  public Long countByEmail(String email);
}
