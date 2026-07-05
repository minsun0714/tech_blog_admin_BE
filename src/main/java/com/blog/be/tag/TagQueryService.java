package com.blog.be.tag;

import com.blog.be.post.domain.PostTagRepository;
import com.blog.be.post.infrastructure.repository.dto.PostTagName;
import com.blog.be.tag.domain.TagRepository;
import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagQueryService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public List<String> findNamesByIdIn(Set<Long> tagIds) {
        return tagRepository.findAllByIdIn(tagIds)
                .stream()
                .map(TagJpaEntity::getName)
                .toList();
    }

    public Map<Long, List<String>> findTagNamesByPostIds(Set<Long> postIds) {
        return postTagRepository.findNamesByPostIds(postIds)
                .stream()
                .collect(Collectors.groupingBy(
                        PostTagName::postId,
                        Collectors.mapping(
                                PostTagName::tagName,
                                Collectors.toList()
                        )
                ));
    }
}
