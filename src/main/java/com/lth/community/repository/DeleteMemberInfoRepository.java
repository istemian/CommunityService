package com.lth.community.repository;

import com.lth.community.entity.DeleteMemberInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteMemberInfoRepository extends JpaRepository<DeleteMemberInfoEntity, Long> {
    DeleteMemberInfoEntity findByMember(MemberInfoEntity member);
}
