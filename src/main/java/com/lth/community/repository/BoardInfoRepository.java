package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardInfoRepository extends JpaRepository<BoardInfoEntity, Long> {
}
