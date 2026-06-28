package com.blog.be.tag.domain;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TagTest {

    @Test
    @DisplayName("태그를 생성한다.")
    void create() {
        // when
        TagJpaEntity tag = TagJpaEntity.create("Spring");

        // then
        assertThat(tag.getName()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("태그 이름을 변경한다.")
    void changeName() {
        // given
        TagJpaEntity tag = TagJpaEntity.create("Spring");

        // when
        tag.changeName("JPA");

        // then
        assertThat(tag.getName()).isEqualTo("JPA");
    }

    @Test
    @DisplayName("빈 이름으로 태그를 생성할 수 없다.")
    void createWithBlankName() {
        assertThatThrownBy(() -> TagJpaEntity.create(""))
                .isInstanceOf(TagException.class);
    }

    @Test
    @DisplayName("빈 이름으로 변경할 수 없다.")
    void changeNameWithBlank() {
        // given
        TagJpaEntity tag = TagJpaEntity.create("Spring");

        // when & then
        assertThatThrownBy(() -> tag.changeName(""))
                .isInstanceOf(TagException.class);
    }
}