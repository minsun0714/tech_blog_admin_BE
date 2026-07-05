package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import com.blog.be.post.infrastructure.repository.projection.PostTagName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PostTagJpaRepository extends JpaRepository<PostTagJpaEntity, Long> {

    Set<PostTagJpaEntity> findAllByPostId(Long postId);

    Set<PostTagJpaEntity> findAllByPostIdIn(Set<Long> postIds);

    @Query("""
        SELECT new com.blog.be.post.infrastructure.repository.projection.PostTagName(
            pt.postId,
            t.name
        )
        FROM PostTagJpaEntity pt
        JOIN TagJpaEntity t
            ON pt.tagId = t.id
        WHERE pt.postId in :postIds
        """)
    List<PostTagName> findNamesByPostIds(@Param("postIds") Set<Long> postIds);

    void deleteAllByPostId(Long postId);
}
