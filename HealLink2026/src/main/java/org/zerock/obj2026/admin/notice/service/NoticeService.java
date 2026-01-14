package org.zerock.obj2026.admin.notice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.admin.notice.domain.Notice;
import org.zerock.obj2026.admin.notice.dto.NoticeAddRequest;
import org.zerock.obj2026.admin.notice.dto.NoticeResponse;
import org.zerock.obj2026.admin.notice.repository.NoticeRepository;
import org.zerock.obj2026.member.domain.User;

import org.zerock.obj2026.member.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository; // 이것도 필요

    // 1. 목록 .findAll
    public List<NoticeResponse> findAll() {
        return noticeRepository.findAllByIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc() // [삭제글은 안보이게] [고정글은 위로] [최신순 조회]
                .stream()
                .map(NoticeResponse::fromEntity) // 2. 정적 메서드로 변환 (::new와 비슷함) 만들었던 fromEntity 사용
                .toList(); // 3. 리스트로 반환
    }

    // 2. 상세 조회 .findById
    @Transactional
    public NoticeResponse findById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다. ID: " + noticeId));

        notice.incrementViewCount(); // 조회수 1증가 : @Transactional 써놔야 DB에 저장됨
        return NoticeResponse.fromEntity(notice); // fromEntity 사용
    }

    // 3. 쓰기 .save
    @Transactional
    public Long createNotice(NoticeAddRequest dto) {
        // 로그인 구현 아직이라서 DB에 저장된 유저 한 명을 가져옵니다 (ID 1번 유저)
        User writer = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없어요."));

        Notice notice = dto.toEntity(writer); // 괄호 안에 writer
        return noticeRepository.save(notice).getNoticeId();

    }

    // 4. 수정 .update(도메인)
    @Transactional
    public void updateNotice(Long noticeId, NoticeAddRequest dto) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("글 id를 사용"));

        // 2. 엔티티의 내용을 수정합니다. (엔티티에 update 메서드를 미리 만들어두면 편합니다)
        notice.update(dto.getTitle(), dto.getContent(), dto.getIsPinned());

    }

    // 5. 삭제 .delete(도메인)
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        notice.delete(); // isDeleted를 true로 변경
    }

}
