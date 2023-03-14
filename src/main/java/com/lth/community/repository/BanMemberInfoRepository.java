package com.lth.community.repository;

import com.lth.community.entity.BanMemberInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanMemberInfoRepository extends JpaRepository<BanMemberInfoEntity, Long> {
    BanMemberInfoEntity findByMember(MemberInfoEntity member);
}
