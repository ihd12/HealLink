package org.zerock.obj2026.admin.notice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.admin.notice.domain.Notice;
import org.zerock.obj2026.admin.notice.dto.NoticeAddRequest;
import org.zerock.obj2026.admin.notice.dto.NoticePageRequestDTO;
import org.zerock.obj2026.admin.notice.dto.NoticePageResponseDTO;
import org.zerock.obj2026.admin.notice.dto.NoticeResponse;
import org.zerock.obj2026.admin.notice.repository.NoticeRepository;
import org.zerock.obj2026.member.domain.User;

import org.zerock.obj2026.member.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository; // 이것도 필요

    // 1. 목록 .findAll
    public NoticePageResponseDTO<NoticeResponse> findAll(NoticePageRequestDTO pageRequestDTO) {

        // 1. 일반글 페이징 조회 (QueryDSL 사용 - 이 안에서 isPinned=false 처리가 되어 있어야 함)
        Page<Notice> result = noticeRepository.searchAll(pageRequestDTO);

        // 2. 고정글 리스트 준비 (기본값은 빈 리스트)
        List<NoticeResponse> pinnedList = new ArrayList<>();

        // [핵심] 검색어가 없을 때만 상단 고정글을 가져옴 (필요에 따라 조건 수정 가능)
        if (pageRequestDTO.getKeyword() == null || pageRequestDTO.getKeyword().trim().isEmpty()) {
            // Repository에 @Query로 만든 메서드 호출
            pinnedList = noticeRepository.findPinnedNotices()
                    .stream()
                    .map(NoticeResponse::fromEntity)
                    .toList();
        }

        // 3. 일반글 리스트 변환
        List<NoticeResponse> dtoList = result.getContent().stream()
                .map(NoticeResponse::fromEntity)
                .toList();

        // 4. 최종 조립 (pinnedList를 추가!)
        return NoticePageResponseDTO.<NoticeResponse>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .pinnedList(pinnedList) // <-- 새로 만든 필드에 주입!
                .total((int)result.getTotalElements())
                .build();
    }

    // 2. 상세 조회 .findById->
    @Transactional
    public NoticeResponse findById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다. ID: " + noticeId));

        notice.incrementViewCount(); // 조회수 1증가 : @Transactional 써놔야 DB에 저장됨
        return NoticeResponse.fromEntity(notice); // fromEntity 사용
    }

    // 3. 쓰기 .save( <- .findByEmail )
    @Transactional
    public Long createNotice(NoticeAddRequest dto, String email) {
        // 로그인 구현 아직이라서 DB에 저장된 유저 한 명을 가져옵니다 (ID 1번 유저)
        User writer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없어요."));

        Notice notice = dto.toEntity(writer); // 괄호 안에 writer
        return noticeRepository.save(notice).getNoticeId();

    }

    // 4. 수정 .update(도메인)
    @Transactional
    public void updateNotice(Long noticeId, NoticeAddRequest dto, Long userId) {
        // 1. 대상 찾기
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("글 id를 사용"));

        // 2. 권한 체크
        if (!notice.getWriter().getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다. 본인만 수정 가능합니다.");
        }

        // 3. 수정 (엔티티의 update 메서드)
        notice.update(dto);

    }

    // 5. 삭제 .delete(도메인)
    @Transactional
    public void deleteNotice(Long noticeId, Long userId) {
        // 1) 대상 찾기
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다. id=" + noticeId));

        // 2) 권한 체크
        if (!notice.getWriter().getUserId().equals(userId)) {
            throw new RuntimeException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        // 3) 삭제 -> (엔티티의 delete 메서드), 주의 : 여기는 소프트 딜리트
        notice.delete();
    }

}
