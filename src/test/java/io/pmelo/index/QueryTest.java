package io.pmelo.index;

import static org.assertj.core.api.Assertions.assertThat;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.Result;
import io.pmelo.crapcene.index.Index;
import io.pmelo.crapcene.index.IndexStorage;
import io.pmelo.crapcene.index.MapIndexStorage;
import io.pmelo.crapcene.index.MVStoreIndexStorage;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class QueryTest {

  abstract static class BaseQueryTest {

    Index<StoredDocument> index;
    IndexStorage<StoredDocument> storage;

    abstract IndexStorage<StoredDocument> createStorage();

    @BeforeEach
    public void setup() {
      storage = createStorage();
      index = new Index<>(storage);
    }

    @AfterEach
    public void tearDown() {
      index.clear();
      storage.close();
    }
  }

  @Nested
  class WithMVStore {

    abstract class MVStoreBase extends BaseQueryTest {
      @Override
      IndexStorage<StoredDocument> createStorage() {
        return new MVStoreIndexStorage<>();
      }
    }

    @Nested
    class WhenQueryingTermsMatchingOneDocument extends MVStoreBase {

      List<Result<StoredDocument>> results;

      Document<StoredDocument> subjectContent =
          Document.<StoredDocument>builder()
              .id(UUID.randomUUID().toString())
              .content("a content")
              .value(new StoredDocument("id"))
              .build();

      @BeforeEach
      void setUp() {

        index.addDocument(subjectContent);

        index.addDocument(
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("nothing")
                .value(new StoredDocument("123"))
                .build());
        index.addDocument(
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("whatever it takes")
                .value(new StoredDocument("456"))
                .build());

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
    class WhenQueryingTermsMatchingMultipleDocuments extends MVStoreBase {

      List<Result<StoredDocument>> results;
      Document<StoredDocument> firstContent;
      Document<StoredDocument> secondContent;
      Document<StoredDocument> thirdContent;
      Document<StoredDocument> fourthContent;

      @BeforeEach
      void setUp() {
        firstContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("a content with multiple content")
                .value(new StoredDocument("id1"))
                .build();

        secondContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content(
                    "this should have the lowest score this content with a higher score because content is repeated multiple content")
                .value(new StoredDocument("id2"))
                .build();

        thirdContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("another content")
                .value(new StoredDocument("id3"))
                .build();

        fourthContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("this should not return")
                .value(new StoredDocument("id4"))
                .build();

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

  @Nested
  class WithMapStorage {

    abstract class MapBase extends BaseQueryTest {
      @Override
      IndexStorage<StoredDocument> createStorage() {
        return new MapIndexStorage<>();
      }
    }

    @Nested
    class WhenQueryingTermsMatchingOneDocument extends MapBase {

      List<Result<StoredDocument>> results;

      Document<StoredDocument> subjectContent =
          Document.<StoredDocument>builder()
              .id(UUID.randomUUID().toString())
              .content("a content")
              .value(new StoredDocument("id"))
              .build();

      @BeforeEach
      void setUp() {

        index.addDocument(subjectContent);

        index.addDocument(
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("nothing")
                .value(new StoredDocument("123"))
                .build());
        index.addDocument(
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("whatever it takes")
                .value(new StoredDocument("456"))
                .build());

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
    class WhenQueryingTermsMatchingMultipleDocuments extends MapBase {

      List<Result<StoredDocument>> results;
      Document<StoredDocument> firstContent;
      Document<StoredDocument> secondContent;
      Document<StoredDocument> thirdContent;
      Document<StoredDocument> fourthContent;

      @BeforeEach
      void setUp() {
        firstContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("a content with multiple content")
                .value(new StoredDocument("id1"))
                .build();

        secondContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content(
                    "this should have the lowest score this content with a higher score because content is repeated multiple content")
                .value(new StoredDocument("id2"))
                .build();

        thirdContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("another content")
                .value(new StoredDocument("id3"))
                .build();

        fourthContent =
            Document.<StoredDocument>builder()
                .id(UUID.randomUUID().toString())
                .content("this should not return")
                .value(new StoredDocument("id4"))
                .build();

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
}
