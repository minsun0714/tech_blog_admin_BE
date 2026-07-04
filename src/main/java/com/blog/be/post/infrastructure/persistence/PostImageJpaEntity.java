package com.blog.be.post.infrastructure.persistence;

import com.blog.be.common.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostImageJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false)
    private boolean thumbnail;

    public static PostImageJpaEntity create(Long postId, String s3Key, boolean thumbnail) {
        return PostImageJpaEntity.builder()
                .postId(postId)
                .s3Key(s3Key)
                .thumbnail(thumbnail)
                .build();
    }
}