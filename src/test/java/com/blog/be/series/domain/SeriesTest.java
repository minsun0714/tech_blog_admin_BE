package com.blog.be.series.domain;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SeriesTest {

    @Test
    @DisplayName("시리즈를 생성한다.")
    void create() {
        // when
        SeriesJpaEntity series = SeriesJpaEntity.create("JPA");

        // then
        assertThat(series.getName()).isEqualTo("JPA");
    }

    @Test
    @DisplayName("시리즈 이름을 변경한다.")
    void changeName() {
        // given
        SeriesJpaEntity series = SeriesJpaEntity.create("JPA");

        // when
        series.changeName("Spring");

        // then
        assertThat(series.getName()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("빈 이름으로 시리즈를 생성할 수 없다.")
    void createWithBlankName() {
        assertThatThrownBy(() -> SeriesJpaEntity.create(""))
                .isInstanceOf(SeriesException.class);
    }

    @Test
    @DisplayName("빈 이름으로 변경할 수 없다.")
    void changeNameWithBlank() {
        // given
        SeriesJpaEntity series = SeriesJpaEntity.create("JPA");

        // when & then
        assertThatThrownBy(() -> series.changeName(""))
                .isInstanceOf(SeriesException.class);
    }
}