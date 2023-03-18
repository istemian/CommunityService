package com.lth.community.repository;

import com.lth.community.entity.FileInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfoEntity, Long> {
}
