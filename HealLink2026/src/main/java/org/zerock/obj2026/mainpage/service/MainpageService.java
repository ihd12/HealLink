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
    public List<NoticeDTO> getLatestNotices() {
        List<NoticeDTO> list = noticeRepository.findTop3ByOrderByCreatedAtDesc().stream()
                .map(NoticeDTO::new)
                .toList();
        return list;
    }
}
