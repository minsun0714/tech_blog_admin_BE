package com.blog.be.tag.infrastructure.repository;

import com.blog.be.tag.domain.TagRepository;
import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public TagJpaEntity save(TagJpaEntity tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public List<TagJpaEntity> saveAll(Set<TagJpaEntity> tags) {
        return tagJpaRepository.saveAll(tags);
    }

    @Override
    public Optional<TagJpaEntity> findById(Long tagId) {
        return tagJpaRepository.findById(tagId);
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
