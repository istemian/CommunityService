package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardInfoRepository extends JpaRepository<BoardInfoEntity, Long> {
    BoardInfoEntity findBySeq(Long seq);
    Page<BoardInfoEntity> findByTitleContains(String title, Pageable pageable);
}
