package com.blog.be.series.infrastructure.persistence;

import com.blog.be.common.infrastructure.persistence.BaseEntity;
import com.blog.be.series.domain.SeriesErrorCode;
import com.blog.be.series.domain.SeriesException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Table(name = "series")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SeriesJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    public static SeriesJpaEntity create(String name) {
        validateName(name);

        return SeriesJpaEntity.builder()
                .name(name)
                .build();
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new SeriesException(SeriesErrorCode.INVALID_SERIES_NAME);
        }
    }
}