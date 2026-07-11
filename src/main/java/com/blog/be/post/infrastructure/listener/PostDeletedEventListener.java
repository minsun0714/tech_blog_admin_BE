package com.blog.be.post.infrastructure.listener;

import com.blog.be.post.application.ImageStorage;
import com.blog.be.post.application.event.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostDeletedEventListener {

    private final ImageStorage imageStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleDelete(PostDeletedEvent postDeletedEvent) {
        imageStorage.deleteMany(postDeletedEvent.postUuid());
    }
}
