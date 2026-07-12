package com.blog.be.post.infrastructure.persistence.mapper;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

import java.util.Set;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostJpaEntity toJpaEntity(Post post, String postUuid) {
        return PostJpaEntity.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .publishStatus(post.getPublishStatus())
                .categoryId(post.getCategoryId())
                .seriesId(post.getSeriesId())
                .postUuid(postUuid)
                .publishStatus(post.getPublishStatus())
                .build();
    }

    public static PostImageJpaEntity toPostImageJpaEntity(
            Long postId,
            PostImage postImage,
            String s3Key
    ) {
        return PostImageJpaEntity.builder()
                .id(postImage.getId())
                .postId(postId)
                .thumbnail(postImage.isThumbnail())
                .s3Key(s3Key)
                .build();
    }

    public static Post toDomain(
            PostJpaEntity postEntity,
            Set<Long> tagIds
    ) {

        return Post.restore(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getPublishStatus(),
                tagIds,
                postEntity.getCategoryId(),
                postEntity.getSeriesId()
        );
    }
}