package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.CommentInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentInfoRepository extends JpaRepository<CommentInfoEntity, Long> {
    CommentInfoEntity findBySeq(Long seq);
    List<CommentInfoEntity> findByMember(MemberInfoEntity member);
}
