package com.lth.community.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBoardVO {
    private List<BoardInfoVO> list;
    private Integer total;
    private Integer totalPage;
    private Integer currentPage;
}
