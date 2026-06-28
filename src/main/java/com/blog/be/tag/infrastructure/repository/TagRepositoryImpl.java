package com.blog.be.tag.infrastructure.repository;

import com.blog.be.tag.domain.TagRepository;
import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public TagJpaEntity save(TagJpaEntity tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public Optional<TagJpaEntity> findById(Long tagId) {
        return tagJpaRepository.findById(tagId);
    }

    @Override
    public Optional<TagJpaEntity> findByName(String name) {
        return tagJpaRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return tagJpaRepository.existsByName(name);
    }

    @Override
    public void delete(TagJpaEntity tag) {
        tagJpaRepository.delete(tag);
    }
}
