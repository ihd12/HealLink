package org.zerock.obj2026.admin.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.admin.notice.domain.Notice;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    // [삭제글은 안보이게] [고정글은 위로] [최신순 조회]
    List<Notice> findAllByIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc();
    List<Notice> findTop3ByOrderByCreatedAtDesc();
}
