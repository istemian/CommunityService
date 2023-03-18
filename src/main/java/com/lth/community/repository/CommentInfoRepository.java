package com.lth.community.repository;

import com.lth.community.entity.CommentInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentInfoRepository extends JpaRepository<CommentInfoEntity, Long> {
    CommentInfoEntity findBySeq(Long seq);
}
