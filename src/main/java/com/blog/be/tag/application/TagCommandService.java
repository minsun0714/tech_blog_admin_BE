package com.blog.be.tag.application;

import com.blog.be.tag.domain.TagErrorCode;
import com.blog.be.tag.domain.TagException;
import com.blog.be.tag.domain.TagRepository;
import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagCommandService {

    private final TagRepository tagRepository;

    public void upsertTag(String name) {
        if (tagRepository.existsByName(name)) return;

        TagJpaEntity tag = TagJpaEntity.create(name);

        tagRepository.save(tag);
    }

    public Set<Long> upsertAllAndGetIds(Set<String> tagNames) {
        Set<TagJpaEntity> tagJpaEntities = tagNames.stream()
                .filter(tagName -> !tagRepository.existsByName(tagName))
                .map(TagJpaEntity::create)
                .collect(Collectors.toSet());

        tagRepository.saveAll(tagJpaEntities);

        return tagRepository.findAllByNameIn(tagNames)
                .stream()
                .map(TagJpaEntity::getId)
                .collect(Collectors.toSet());
    }

    public void updateTagName(Long id, String newName) {
        TagJpaEntity tag = getTag(id);

        tag.changeName(newName);
    }

    public void deleteTag(Long id){
        TagJpaEntity tag = getTag(id);

        tagRepository.delete(tag);
    }

    private TagJpaEntity getTag(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagException(TagErrorCode.TAG_NOT_FOUND));
    }
}
