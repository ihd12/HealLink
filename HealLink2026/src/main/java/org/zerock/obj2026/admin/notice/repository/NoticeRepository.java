package org.zerock.obj2026.admin.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.obj2026.admin.notice.domain.Notice;

import java.util.List;

// [삭제글은 안보이게] [고정글은 위로] [최신순 조회]
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeSearch {
    @Query("SELECT n FROM Notice n WHERE n.isPinned = true AND n.isDeleted = false ORDER BY n.noticeId DESC")
    List<Notice> findPinnedNotices();

}

/* QueryDSL 쓰기 전에 쿼리메소드 코드가 이랬음
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    List<Notice> findAllByIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc();
    Page<Notice> findAllByIsDeletedFalse(Pageable pageable);
}
*/
