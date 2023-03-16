package com.lth.community.vo.board;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBoardVO {
    private List<BoardInfoVO> list;
    private Long total;
    private Integer totalPage;
    private Integer currentPage;
}
