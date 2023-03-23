package com.lth.community.repository;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.FileInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfoEntity, Long> {
    FileInfoEntity findByFilename(String filename);
    List<FileInfoEntity> findByBoard(BoardInfoEntity board);
}
