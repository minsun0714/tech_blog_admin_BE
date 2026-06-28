package com.blog.be.post.infrastructure.persistence;

import com.blog.be.common.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "post_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostTagJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long tagId;

    private PostTagJpaEntity(Long postId, Long tagId){
        this.postId = postId;
        this.tagId = tagId;
    }

    public static PostTagJpaEntity create(Long postId, Long tagId) {
        return new PostTagJpaEntity(postId, tagId);
    }
}