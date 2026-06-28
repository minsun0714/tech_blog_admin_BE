package com.blog.be.post.infrastructure.persistence.mapper;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

import java.util.List;
import java.util.Set;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostJpaEntity toJpaEntity(Post post) {
        return PostJpaEntity.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .openStatus(post.getOpenStatus())
                .categoryId(post.getCategoryId())
                .seriesId(post.getSeriesId())
                .likeCount(post.getLikeCount())
                .build();
    }

    public static List<PostImageJpaEntity> toPostImageJpaEntities(
            Long postId,
            List<PostImage> postImages
    ) {
        return postImages.stream()
                .map(image -> PostImageJpaEntity.builder()
                        .id(image.getId())
                        .postId(postId)
                        .thumbnail(image.isThumbnail())
                        .build())
                .toList();
    }

    public static Post toDomain(
            PostJpaEntity postEntity,
            List<PostImageJpaEntity> imageEntities,
            Set<Long> tagIds
    ) {
        List<PostImage> images = imageEntities.stream()
                .map(image -> PostImage.create(
                        image.getId(),
                        image.isThumbnail()
                ))
                .toList();

        return Post.restore(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getOpenStatus(),
                images,
                tagIds,
                postEntity.getCategoryId(),
                postEntity.getSeriesId(),
                postEntity.getLikeCount()
        );
    }
}