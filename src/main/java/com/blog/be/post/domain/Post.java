package com.blog.be.post.domain;

import com.blog.be.post.domain.image.PostImage;
import lombok.Getter;

import java.util.*;

@Getter
public class Post {

    private Long postId;

    private String title;

    private String content;

    private OpenStatus openStatus;

    private Set<Long> tagIds;

    public Set<Long> getTagIds() {
        return Collections.unmodifiableSet(tagIds);
    }

    private Long categoryId;

    private Long seriesId;

    private Post(String title, String content, OpenStatus openStatus, Set<Long> tagIds, Long categoryId, Long seriesId) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        Objects.requireNonNull(openStatus);
        Objects.requireNonNull(categoryId);

        this.title = title;
        this.content = content;
        this.openStatus = openStatus;
        this.tagIds = new HashSet<>(tagIds);
        this.categoryId = categoryId;
        this.seriesId = seriesId;
    }

    private Post(
            Long postId,
            String title,
            String content,
            OpenStatus openStatus,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ) {
        Objects.requireNonNull(postId);
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        Objects.requireNonNull(openStatus);
        Objects.requireNonNull(tagIds);
        Objects.requireNonNull(categoryId);

        this.postId = postId;
        this.title = title;
        this.content = content;
        this.openStatus = openStatus;
        this.tagIds = new HashSet<>(tagIds);
        this.categoryId = categoryId;
        this.seriesId = seriesId;
    }

    public static Post restore(
            Long postId,
            String title,
            String content,
            OpenStatus openStatus,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ) {
        return new Post(
                postId,
                title,
                content,
                openStatus,
                tagIds,
                categoryId,
                seriesId
        );
    }

    public static Post publish(
            String title,
            String content,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ) {
        return new Post(
          title,
          content,
          OpenStatus.PUBLIC,
          tagIds,
          categoryId,
          seriesId
        );
    }

    public static Post draft(
            String title,
            String content,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ) {
        return new Post(
                title,
                content,
                OpenStatus.PRIVATE,
                tagIds,
                categoryId,
                seriesId
        );
    }

    public void change(
            String title,
            String content,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ){
        changeTitle(title);
        changeContent(content);
        changeCategory(categoryId);
        changeSeries(seriesId);
        changeTags(tagIds);
    }

    public void changeTitle(String title) {
        Objects.requireNonNull(title);

        this.title = title;
    }

    public void changeContent(String content) {
        Objects.requireNonNull(content);

        this.content = content;
    }

    public void addTag(Long tagId) {
        Objects.requireNonNull(tagId);

        tagIds.add(tagId);
    }

    public void removeTag(Long tagId) {
        Objects.requireNonNull(tagId);

        tagIds.remove(tagId);
    }

    public void changeTags(Set<Long> tagIds) {
        Objects.requireNonNull(tagIds);
        this.tagIds = new HashSet<>(tagIds);
    }

    public void changeCategory (Long categoryId){
        Objects.requireNonNull(categoryId);

        this.categoryId = categoryId;
    }

    public void changeSeries(Long seriesId) {
        this.seriesId = seriesId;
    }



}
