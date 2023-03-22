package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardInfoRepository extends JpaRepository<BoardInfoEntity, Long> {
    BoardInfoEntity findBySeq(Long seq);
    List<BoardInfoEntity> findByMember(MemberInfoEntity member);
    @Query(value = "SELECT * FROM board_info bi WHERE board_title like %:keyword% order by board_seq desc limit :page, :size", nativeQuery = true)
    List<BoardInfoEntity> findByTitle(@Param("keyword") String keyword, @Param("page") Integer page, @Param("size") Integer size);
    @Query(value = "SELECT a FROM BoardInfoEntity a WHERE a.title like %:keyword%")
    List<BoardInfoEntity> findByTitleAll(@Param("keyword") String keyword);
}
