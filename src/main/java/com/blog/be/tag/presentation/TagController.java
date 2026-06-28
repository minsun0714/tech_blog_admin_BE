package com.blog.be.tag.presentation;

import com.blog.be.tag.application.TagCommandService;
import com.blog.be.tag.presentation.dto.TagCreateRequest;
import com.blog.be.tag.presentation.dto.TagUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagCommandService tagCommandService;

    @PostMapping
    public ResponseEntity<Void> createTag(
            @RequestBody TagCreateRequest request
    ) {
        tagCommandService.upsertTag(request.name());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTagName(
            @PathVariable Long id,
            @RequestBody TagUpdateRequest request
    ) {
        tagCommandService.updateTagName(id, request.name());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long id
    ) {
        tagCommandService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }
}