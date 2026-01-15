// QueryDSL용

package org.zerock.obj2026.admin.notice.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.obj2026.admin.notice.domain.Notice;
import org.zerock.obj2026.admin.notice.domain.QNotice; // domain 패키지 확인!
import org.zerock.obj2026.admin.notice.dto.NoticePageRequestDTO;

import java.util.List;

public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {

    public NoticeSearchImpl() {
        super(Notice.class);
    }

    @Override
    public Page<Notice> searchAll(NoticePageRequestDTO pageRequestDTO) {
        QNotice notice = QNotice.notice;
        JPQLQuery<Notice> query = from(notice);

        String keyword = pageRequestDTO.getKeyword();
        String[] types = pageRequestDTO.splitTypes();

        // 1. [추가] 일반 목록에서는 고정글을 제외 (중복 방지)
        // 이렇게 해야 상단 고정 리스트와 데이터가 겹치지 않습니다.
        query.where(notice.isPinned.eq(false));

        // 2. 삭제되지 않은 글만 (기존 조건)
        query.where(notice.isDeleted.eq(false));

        // 3. 검색 조건 처리
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(notice.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(notice.content.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        // 4. 페이징 적용
        Pageable pageable = pageRequestDTO.getPageable();
        this.getQuerydsl().applyPagination(pageable, query);

        List<Notice> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}