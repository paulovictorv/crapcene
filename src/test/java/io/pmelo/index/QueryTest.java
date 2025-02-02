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
        Document<StoredDocument> subjectContent = Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("a content")
                .value(new StoredDocument("id"))
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

        @Test
        void itShouldReturnTheCorrectValue() {
            assertThat(results.getFirst().document().value()).isEqualTo(new StoredDocument("id"));
        }

    }

    @Nested
    class WhenQueryingTermsMatchingMultipleDocuments {

        List<Result> results;
        Document<StoredDocument> firstContent = Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("a content with multiple content")
                .value(new StoredDocument("id1"))
                .build();

        Document<StoredDocument> secondContent = Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("this should have the lowest score this content with a higher score because content is repeated multiple content")
                .value(new StoredDocument("id2"))
                .build();

        Document<StoredDocument> thirdContent = Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("another content")
                .value(new StoredDocument("id3"))
                .build();

        Document<StoredDocument> fourthContent = Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("this should not return")
                .value(new StoredDocument("id4"))
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
        void itShouldReturnTheMostRelevantDocumentFirst() {
            assertThat(results.getFirst().document()).isEqualTo(firstContent);
        }

        @Test
        void itShouldReturnTheCorrectAttachedValue() {
            assertThat(results.getFirst().document().value()).isEqualTo(new StoredDocument("id1"));
        }

    }

}
