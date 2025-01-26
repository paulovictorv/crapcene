package io.pmelo.index;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.index.Index;
import io.pmelo.crapcene.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AddDocumentTest {

    private final Index index = new Index();

    @Nested
    class WhenAddingADocument {

        List<Result> content;

        @BeforeEach
        void addDocument() {
            index.addDocument(
                    Document.builder()
                            .id(UUID.randomUUID().toString())
                            .content("a content")
                            .build()
            );
            content = index.query("content");
        }

        @Test
        void itShouldAddDocument() {
            assertThat(content).hasSizeGreaterThanOrEqualTo(1);
        }

    }

}
