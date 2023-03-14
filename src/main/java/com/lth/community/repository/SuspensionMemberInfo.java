package com.lth.community.repository;

import com.lth.community.entity.SuspensionMemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspensionMemberInfo extends JpaRepository<SuspensionMemberInfoEntity, Long> {
}
