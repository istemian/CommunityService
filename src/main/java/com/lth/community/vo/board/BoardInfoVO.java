package com.lth.community.vo.board;

import com.lth.community.entity.BoardInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardInfoVO {
    private Long no;
    private String nickname;
    private String title;
    private LocalDateTime creatDt;
    private LocalDateTime modifiedDt;

    public BoardInfoVO(BoardInfoEntity board) {
        this.no = board.getSeq();
        if(board.getBoardId() != null) {
            this.nickname = board.getBoardId();
        }
        else if(board.getMember().getMemberId() != null) {
            this.nickname = board.getMember().getMemberId();
        }
        this.title = board.getTitle();
        this.creatDt = board.getCreatDt();
        this.modifiedDt = board.getModifiedDt();
    }
}
