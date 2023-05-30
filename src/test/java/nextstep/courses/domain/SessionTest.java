package nextstep.courses.domain;

import nextstep.courses.data.CourseMaker;
import nextstep.courses.data.SessionMaker;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = SessionMaker.makeSession(CourseMaker.makeCourse());
    }

    @DisplayName("강의 상태가 모집중이 아닐경우 신청 시 에러를 던진다.")
    @Test
    void 강의_비모집_에러() {
        assertThatThrownBy(() -> {
            session.apply(0L, NsUserTest.JAVAJIGI);
        }).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("강의모집인원 마감시 true를 리턴한다.")
    @Test
    void 강의_모집인원_초과_에러() {
        session.recruiting();
        assertThatThrownBy(() -> {
            session.apply(0L, NsUserTest.JAVAJIGI);
            session.apply(1L, NsUserTest.SANJIGI);
        }).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("신청시 생성되는 신청정보에 결과 검증")
    @Test
    void 신청정보_생성_결과검증() {
        session.recruiting();
        assertThat(session.apply(0L, NsUserTest.SANJIGI)).isEqualTo(
                new SessionUser(0L, session, NsUserTest.SANJIGI)
        );
    }

}