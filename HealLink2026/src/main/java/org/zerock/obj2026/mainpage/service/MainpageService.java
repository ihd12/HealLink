package org.zerock.obj2026.mainpage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.admin.notice.repository.NoticeRepository;
import org.zerock.obj2026.notice.NoticeDTO;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MainpageService {

    private final NoticeRepository noticeRepository;
    // 최신 공지 2개 반환 (더미 데이터)
    public List<NoticeDTO> getLatestNotices() {
        List<NoticeDTO> list = noticeRepository.findAll().stream()
                .map(NoticeDTO::new)
                .toList();
        return list;
    }
}
