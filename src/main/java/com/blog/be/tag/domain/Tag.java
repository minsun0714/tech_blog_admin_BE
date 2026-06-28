package com.blog.be.tag.domain;

import lombok.Getter;

import java.util.Objects;

public class Tag {

    private TagId tagId;

    @Getter
    private String name;

    private Tag(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public static Tag create(String name) {
        Objects.requireNonNull(name);
        validateNameNotBlank(name);

        return new Tag(name);
    }

    public void changeName(String name) {
        Objects.requireNonNull(name);
        validateNameNotBlank(name);

        this.name = name;
    }

    private static void validateNameNotBlank(String name) {
        if (name.isBlank()){
            throw new TagException(TagErrorCode.INVALID_TAG_NAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tagId);
    }
}
