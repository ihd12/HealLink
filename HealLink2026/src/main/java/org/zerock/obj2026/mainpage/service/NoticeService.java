package org.zerock.obj2026.mainpage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.domain.Notice;
import org.zerock.obj2026.dto.NoticeDTO;
import org.zerock.obj2026.repository.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    // 최신 공지 2개 반환 (더미 데이터)
    public List<NoticeDTO> getLatestNotices() {
        List<NoticeDTO> list = noticeRepository.findAll().stream()
                .map(NoticeDTO::new)
                .toList();
        return list;
    }
}
