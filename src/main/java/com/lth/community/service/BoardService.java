package com.lth.community.service;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.FileInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.BoardInfoRepository;
import com.lth.community.repository.FileRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.*;
import com.lth.community.vo.comment.GetCommentVO;
import com.lth.community.vo.file.GetFileVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardInfoRepository boardInfoRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder encoder;
    @Value("${file.files}") String path;

    public MessageVO writing(String memberId, WritingMemberVO member, MultipartFile[] files) {
        MemberInfoEntity memberCheck = memberInfoRepository.findByMemberId(memberId);
        Path folderLocation = Paths.get(path);
        if(member.getTitle() == null || member.getTitle().equals("") || member.getContent() == null || member.getContent().equals("")) {
            return MessageVO.builder()
                    .status(false)
                    .message("글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BoardInfoEntity memberPost = BoardInfoEntity.builder()
                .title(member.getTitle())
                .content(member.getContent())
                .member(memberCheck)
                .creatDt(LocalDateTime.now())
                .build();
        boardInfoRepository.save(memberPost);
        if(files != null) {
            for (int a = 0; a < files.length; a++) {
                String originFileName = files[a].getOriginalFilename();
                String saveFilename = String.valueOf(UUID.randomUUID());
                Path targetFile = folderLocation.resolve(saveFilename);
                try {
                    Files.copy(files[a].getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileInfoEntity file = new FileInfoEntity(null, saveFilename, originFileName, memberPost);
                fileRepository.save(file);
            }
        }
        return MessageVO.builder()
                .status(true)
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO nonWriting(WritingNonMemberVO nonMember, MultipartFile[] files) {
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String pwPattern = "^[0-9|a-z|A-Z]*$";
        Path folderLocation = Paths.get(path);

        if(nonMember.getId() == null || nonMember.getId().equals("") || nonMember.getPw() == null || nonMember.getPw().equals("") || nonMember.getTitle() == null || nonMember.getTitle().equals("") || nonMember.getContent() == null || nonMember.getContent().equals("")) {
            return MessageVO.builder()
                    .status(false)
                    .message("게시글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(namePattern, nonMember.getId())) {
            return MessageVO.builder()
                    .status(false)
                    .message("아이디를 다시 확인해주세요. (특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(pwPattern, nonMember.getPw()) || nonMember.getPw().length() < 4) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 다시 확인해주세요. (영문과 숫자만 가능, 4자리 이상)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BoardInfoEntity nonMemberPost = BoardInfoEntity.builder()
                .boardId(nonMember.getId()+"(비회원)")
                .pw(encoder.encode(nonMember.getPw()))
                .title(nonMember.getTitle())
                .content(nonMember.getContent())
                .creatDt(LocalDateTime.now())
                .build();
        boardInfoRepository.save(nonMemberPost);
        if(files != null) {
            for (int a = 0; a < files.length; a++) {
                String originFileName = files[a].getOriginalFilename();
                String saveFilename = String.valueOf(UUID.randomUUID());
                Path targetFile = folderLocation.resolve(saveFilename);
                try {
                    Files.copy(files[a].getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileInfoEntity file = new FileInfoEntity(null, saveFilename, originFileName, nonMemberPost);
                fileRepository.save(file);
            }
        }
        return MessageVO.builder()
                .status(true)
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO delete(String memberId, Long no) {
        BoardInfoEntity board = boardInfoRepository.findBySeq(no);
        MessageVO response = null;
        if(board == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Boolean check = checkId(memberId, board);
        if(check) {
            boardInfoRepository.delete(board);
            response = MessageVO.builder()
                    .status(true)
                    .message("삭제되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            response = MessageVO.builder()
                    .status(false)
                    .message("삭제 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return response;
    }

    public MessageVO nonDelete(Long no, DeletePostNonMemberVO data) {
        BoardInfoEntity board = boardInfoRepository.findBySeq(no);
        MessageVO response = null;
        String message = null;
        if(board == null) {
            response = MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(encoder.matches(data.getPw(), board.getPw())) {
            boardInfoRepository.delete(board);
            response = MessageVO.builder()
                    .status(true)
                    .message("삭제되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            if(!encoder.matches(data.getPw(), board.getPw())) {
                message = "비밀번호가 일치하지 않습니다.";
            }
            else {
                message = "삭제 실패했습니다.";
            }
            response = MessageVO.builder()
                    .status(false)
                    .message(message)
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return response;
    }

    public GetBoardVO getBoard(String keyword, Pageable pageable) {
        if(keyword == null) { keyword = ""; }
        Page<BoardInfoEntity> board = boardInfoRepository.findByTitleContains(keyword, pageable);
        List<BoardInfoVO> info = new ArrayList<>();
        if(pageable.getPageNumber() > board.getTotalPages()-1) {
            return null;
        }
        for(int i=0; i<10; i++) {
            if (board.getContent().get(i).getBoardId() == null) {
                BoardInfoVO infoMake = BoardInfoVO.builder()
                        .no(board.getContent().get(i).getSeq())
                        .nickname(board.getContent().get(i).getMember().getMemberId())
                        .title(board.getContent().get(i).getTitle())
                        .creatDt(board.getContent().get(i).getCreatDt())
                        .modifiedDt(board.getContent().get(i).getModifiedDt())
                        .build();
                info.add(infoMake);
            }
            else if(board.getContent().get(i).getMember() == null) {
                BoardInfoVO infoMake = BoardInfoVO.builder()
                        .no(board.getContent().get(i).getSeq())
                        .nickname(board.getContent().get(i).getBoardId())
                        .title(board.getContent().get(i).getTitle())
                        .creatDt(board.getContent().get(i).getCreatDt())
                        .modifiedDt(board.getContent().get(i).getModifiedDt())
                        .build();
                info.add(infoMake);
            }
        }
        return GetBoardVO.builder()
                .list(info)
                .total(board.getTotalElements())
                .totalPage(board.getTotalPages())
                .currentPage(board.getNumber()+1)
                .build();

    }

    public MessageVO update(WritingMemberVO data, String memberId, Long no) {
        BoardInfoEntity post = boardInfoRepository.findBySeq(no);
        if(post == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Boolean check = checkId(memberId, post);
        if(check) {
            if(data.getTitle() == null || data.getTitle().equals("")) {
                post.setTitle(post.getTitle());
                post.setContent(data.getContent());
            }
            else if(data.getContent() == null || data.getContent().equals("")) {
                post.setTitle(data.getTitle());
                post.setContent(post.getContent());
            }
            else {
                post.setTitle(data.getTitle());
                post.setContent(data.getContent());
            }
            post.setModifiedDt(LocalDateTime.now());
            boardInfoRepository.save(post);
            return MessageVO.builder()
                    .status(true)
                    .message("수정되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            return MessageVO.builder()
                    .status(false)
                    .message("본인의 글이 아닙니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public MessageVO nonUpdate(UpdatePostNonMember data, Long no) {
        BoardInfoEntity post = boardInfoRepository.findBySeq(no);
        String message = null;
        if(post == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getPw() == null || !encoder.matches(data.getPw(), post.getPw())) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 확인해주세요.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }

        if(data.getTitle() == null || data.getTitle().equals("")) {
            post.setTitle(post.getTitle());
            post.setContent(data.getContent());
        }
        else if(data.getContent() == null || data.getContent().equals("")) {
            post.setTitle(data.getTitle());
            post.setContent(post.getContent());
        }
        else {
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
        }
        post.setModifiedDt(LocalDateTime.now());
        boardInfoRepository.save(post);
        return MessageVO.builder()
                .status(true)
                .message("수정되었습니다.")
                .code(HttpStatus.OK)
                .build();

    }
    public ResponseEntity<Resource> getFile(String filename) throws Exception {
        Path folderLocation = Paths.get(path);
        FileInfoEntity file = fileRepository.findByFilename(filename);
        Path targetFile = folderLocation.resolve(filename);
        UrlResource resource = new UrlResource(targetFile.toUri());
        String encodedFileName = UriUtils.encode(file.getOriginalName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
    }

    public BoardDetailVO getDetail(Long no) {
        BoardInfoEntity board = boardInfoRepository.findBySeq(no);
        String commentNickname = null;
        String boardNickname = null;
        if(board == null) {
            return null;
        }
        List<GetCommentVO> commentInfo = new ArrayList<>();
        List<GetFileVO> fileInfo = new ArrayList<>();
        if(board.getComments() != null) {
            for(int i=0; i<board.getComments().size(); i++) {
                if(board.getComments().get(i).getNickname() == null) {
                    commentNickname = board.getComments().get(i).getMember().getNickname();
                }
                if(board.getComments().get(i).getMember() == null) {
                    commentNickname = board.getComments().get(i).getNickname();
                }
                GetCommentVO commentMake = GetCommentVO.builder()
                        .no(board.getComments().get(i).getSeq())
                        .nickname(commentNickname)
                        .comment(board.getComments().get(i).getContent())
                        .createDt(board.getComments().get(i).getCreatDt())
                        .build();
                commentInfo.add(commentMake);
            }
        }
        if(board.getFiles() != null) {
            for(int i=0; i<board.getFiles().size(); i++) {
                GetFileVO fileMake = new GetFileVO(board.getFiles().get(i).getFilename() ,board.getFiles().get(i).getOriginalName());
                fileInfo.add(fileMake);
            }
        }
        if(board.getMember() == null) {
            boardNickname = board.getBoardId();
        }
        else if (board.getBoardId() == null) {
            boardNickname = board.getMember().getNickname();
        }
        return BoardDetailVO.builder()
                .no(board.getSeq())
                .nickname(boardNickname)
                .title(board.getTitle())
                .content(board.getContent())
                .creatDt(board.getCreatDt())
                .modifiedDt(board.getModifiedDt())
                .files(fileInfo)
                .comments(commentInfo)
                .build();
    }

    public Boolean checkId(String memberId, BoardInfoEntity board) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        if(member == board.getMember()) {
            return true;
        }
        else {
            return false;
        }
    }
}
