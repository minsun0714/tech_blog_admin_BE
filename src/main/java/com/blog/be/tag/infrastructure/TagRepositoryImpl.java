package com.blog.be.tag.infrastructure;

import com.blog.be.category.domain.Category;
import com.blog.be.category.domain.CategoryId;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.CategoryJpaRepository;
import com.blog.be.tag.domain.Tag;
import com.blog.be.tag.domain.TagId;
import com.blog.be.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public Optional<Tag> findById(TagId tagId) {
        return tagJpaRepository.findById(tagId.id());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tagJpaRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return tagJpaRepository.existsByName(name);
    }

    @Override
    public void delete(Tag tag) {
        tagJpaRepository.delete(tag);
    }
}
