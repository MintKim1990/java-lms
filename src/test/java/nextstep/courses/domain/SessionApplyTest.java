package nextstep.courses.domain;

import nextstep.courses.data.CourseMaker;
import nextstep.courses.data.SessionMaker;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SessionApplyTest {

    private SessionApply sessionApply;
    private Session session;

    @BeforeEach
    void setUp() {
        sessionApply = new SessionApply(1);
        session = SessionMaker.makeSession(CourseMaker.makeCourse());
    }

    @DisplayName("강의모집인원이 음수일경우 에러를 던진다.")
    @Test
    void 강의_모집인원_음수_에러() {
        assertThatThrownBy(() -> {
            new SessionApply(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("강의 상태가 모집중이 아닐경우 신청 시 에러를 던진다.")
    @Test
    void 강의_비모집_에러() {
        assertThatThrownBy(() -> {
            sessionApply.apply(0L, session, NsUserTest.JAVAJIGI);
        }).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("강의모집인원 마감시 true를 리턴한다.")
    @Test
    void 강의_모집인원_초과_에러() {
        sessionApply.recruiting();
        assertThatThrownBy(() -> {
            sessionApply.apply(0L, session, NsUserTest.JAVAJIGI);
            sessionApply.apply(1L, session, NsUserTest.SANJIGI);
        }).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("신청시 생성되는 신청정보에 결과 검증")
    @Test
    void 신청정보_생성_결과검증() {
        sessionApply.recruiting();
        assertThat(sessionApply.apply(0L, session, NsUserTest.SANJIGI)).isEqualTo(
                new SessionUser(0L, session, NsUserTest.SANJIGI)
        );
    }

}