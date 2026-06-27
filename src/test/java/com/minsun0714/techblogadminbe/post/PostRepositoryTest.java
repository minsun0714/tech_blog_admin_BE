package com.minsun0714.techblogadminbe.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void saveAndFindByTitle() {
        Post post = postRepository.save(Post.builder()
                .title("Spring Boot 3 with Lombok and JPA")
                .build());

        assertThat(postRepository.findByTitle(post.getTitle()))
                .isPresent()
                .get()
                .extracting(Post::getId, Post::getTitle)
                .containsExactly(post.getId(), post.getTitle());
    }
}
