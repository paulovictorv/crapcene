package io.pmelo.index;

import static org.assertj.core.api.Assertions.assertThat;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.Result;
import io.pmelo.crapcene.index.Index;
import io.pmelo.crapcene.index.MapIndexStorage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AddDocumentTest {

  private final Index<StoredDocument> index = new Index<>(new MapIndexStorage<>());

  @Nested
  class WhenAddingADocument {

    List<Result<StoredDocument>> content;

    @BeforeEach
    void addDocument() {
      index.addDocument(
          Document.<StoredDocument>builder()
              .id("id")
              .content("content")
              .value(new StoredDocument("id"))
              .build());
      content = index.query("content");
    }

    @Test
    void itShouldAddDocument() {
      assertThat(content).hasSizeGreaterThanOrEqualTo(1);
    }
  }
}
