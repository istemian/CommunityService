package com.lth.community.repository;

import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfoEntity, Long> {
  public MemberInfoEntity findByMemberIdAndPw(String memberId, String pw);
  public Integer countByMemberId(String memberId);
  public Page<MemberInfoEntity> findByMemberIdContains(String memberId, Pageable pageable);
  public MemberInfoEntity findByMemberId(String memberId);
}
