package com.blog.be.series.infrastructure;

import com.blog.be.comment.domain.Comment;
import com.blog.be.series.domain.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesJpaRepository extends JpaRepository<Series, Long> {
}
