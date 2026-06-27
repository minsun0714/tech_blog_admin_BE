package com.minsun0714.techblogadminbe.post;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);
}
