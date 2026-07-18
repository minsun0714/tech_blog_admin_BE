package com.blog.be.post.infrastructure.persistence;

import com.blog.be.common.infrastructure.persistence.BaseEntity;
import com.blog.be.post.domain.PublishStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublishStatus publishStatus;

    @Column(nullable = false)
    private Long categoryId;

    @Column
    private Long seriesId;

    @Column
    private String postUuid;

    @Column
    private String thumbnailImageUrl;
}
