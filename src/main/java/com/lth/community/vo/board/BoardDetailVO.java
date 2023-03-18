package com.lth.community.vo.board;

import com.lth.community.entity.CommentInfoEntity;
import com.lth.community.entity.FileInfoEntity;
import com.lth.community.vo.comment.GetCommentVO;
import com.lth.community.vo.file.GetFileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailVO {
    private Long no;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime creatDt;
    private LocalDateTime modifiedDt;
    private List<GetFileVO> files;
    private List<GetCommentVO> comments;
}
