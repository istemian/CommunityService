package com.lth.community.repository;

import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfoEntity, Long> {
  MemberInfoEntity findByMemberId(String memberId);
  MemberInfoEntity findByEmail(String email);
  Long countByMemberIdAndNameAndEmail(String memberId, String name, String email);
  Long countByMemberId(String memberId);
  Long countByNickname(String nickname);
  Long countByEmail(String email);
}
