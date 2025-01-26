package io.pmelo.index;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.Result;
import io.pmelo.crapcene.index.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {

    private final Index index = new Index();

    @Nested
    class WhenQueryingTermsMatchingOneDocument {

        List<Result> results;
        Document subjectContent = Document.builder()
                .id(UUID.randomUUID().toString())
                .content("a content")
                .build();

        @BeforeEach
        void setUp() {

            index.addDocument(
                    subjectContent
            );
            index.addDocument(
                    Document.builder()
                            .id(UUID.randomUUID().toString())
                            .content("nothing")
                            .build()
            );
            index.addDocument(
                    Document.builder()
                            .id(UUID.randomUUID().toString())
                            .content("whatever it takes")
                            .build()
            );

            results = index.query("content");
        }

        @Test
        void itShouldReturnOnlyOneDocument() {
            assertThat(results.size()).isEqualTo(1);
        }

        @Test
        void itShouldReturnTheCorrectDocument() {
            assertThat(results.getFirst().document()).isEqualTo(subjectContent);
        }

    }

    @Nested
    class WhenQueryingTermsMatchingMultipleDocuments {

        List<Result> results;
        Document firstContent = Document.builder()
                .id(UUID.randomUUID().toString())
                .content("a content with multiple content")
                .build();

        Document secondContent = Document.builder()
                .id(UUID.randomUUID().toString())
                .content("this should have the lowest score this content with a higher score because content is repeated multiple content")
                .build();

        Document thirdContent = Document.builder()
                .id(UUID.randomUUID().toString())
                .content("another content")
                .build();

        Document fourthContent = Document.builder()
                .id(UUID.randomUUID().toString())
                .content("this should not return")
                .build();

        @BeforeEach
        void setUp() {
            index.addDocument(firstContent);
            index.addDocument(secondContent);
            index.addDocument(thirdContent);
            index.addDocument(fourthContent);

            results = index.query("content");
        }

        @Test
        void itShouldReturnExactlyThreeDocuments() {
            assertThat(results.size()).isEqualTo(3);
        }

        @Test
        void itShouldReturnTheCorrectDocument() {
            assertThat(results.getFirst().document()).isEqualTo(firstContent);
        }

    }

}
