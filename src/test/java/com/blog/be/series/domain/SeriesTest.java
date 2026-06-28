package com.blog.be.series.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SeriesTest {

    @Test
    @DisplayName("시리즈를 생성한다.")
    void create() {
        // when
        Series series = Series.create("JPA");

        // then
        assertThat(series.getName()).isEqualTo("JPA");
    }

    @Test
    @DisplayName("시리즈 이름을 변경한다.")
    void changeName() {
        // given
        Series series = Series.create("JPA");

        // when
        series.changeName("Spring");

        // then
        assertThat(series.getName()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("빈 이름으로 시리즈를 생성할 수 없다.")
    void createWithBlankName() {
        assertThatThrownBy(() -> Series.create(""))
                .isInstanceOf(SeriesException.class);
    }

    @Test
    @DisplayName("빈 이름으로 변경할 수 없다.")
    void changeNameWithBlank() {
        // given
        Series series = Series.create("JPA");

        // when & then
        assertThatThrownBy(() -> series.changeName(""))
                .isInstanceOf(SeriesException.class);
    }
}