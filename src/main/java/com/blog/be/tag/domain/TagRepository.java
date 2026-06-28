package com.blog.be.tag.domain;

import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findById(TagId tagId);

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    void delete(Tag tag);
}