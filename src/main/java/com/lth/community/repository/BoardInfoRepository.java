package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardInfoRepository extends JpaRepository<BoardInfoEntity, Long> {
    BoardInfoEntity findBySeq(Long seq);
    List<BoardInfoEntity> findByMember(MemberInfoEntity member);
    Page<BoardInfoEntity> findByTitleContains(String title, Pageable pageable);
}
