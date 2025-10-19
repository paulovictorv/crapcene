package io.pmelo.crapcene.index;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.Result;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Index<T> {

  private final float K = 1.2f;
  private final float B = 0.75f;

  private final Function<String, Double> inverseDocumentFrequency;

  private final Function<String, Stream<String>> tokenizer;

  IndexStorage<T> indexStorage;

  private double averageDocumentLength = 0d;

  public Index(IndexStorage<T> storage) {
    indexStorage = storage;
    inverseDocumentFrequency =
        (term) ->
            Math.log(
                (indexStorage.size() - this.documentsWithTerm(term) + 0.5)
                        / (this.documentsWithTerm(term) + 0.5)
                    + 1);
    tokenizer =
        (content) ->
            Arrays.stream(content.split(" "))
                .map(String::strip)
                .filter(str -> !str.isEmpty())
                .map(String::toLowerCase);
  }

  public void addDocument(Document<T> document) {
    Map<String, Integer> termCount =
        tokenizer
            .apply(document.content())
            .collect(Collectors.toMap(String::toString, string -> 1, Integer::sum));

    final InternalDocument<T> updatedDocument =
        InternalDocument.<T>builder().document(document).termCount(termCount).build();

    Set<String> terms = termCount.keySet();

    this.indexStorage.add(terms, updatedDocument);
    // TODO refactor to a cumulative average
    this.averageDocumentLength =
        indexStorage.documents().stream().mapToInt(InternalDocument::length).average().orElse(0d);
  }

  public int documentsWithTerm(String term) {
    return indexStorage.documentsWithTerm(term).size();
  }

  public List<Result<T>> query(String query) {
    return indexStorage.documents().stream()
        .map(
            internalDocument -> {
              double score =
                  tokenizer
                      .apply(query)
                      .mapToDouble(
                          queryTerm ->
                              this.inverseDocumentFrequency.apply(queryTerm)
                                  * ((internalDocument.termCount(queryTerm) * (K + 1))
                                      / (internalDocument.termCount(queryTerm)
                                          + K
                                              * (1
                                                  - B
                                                  + B
                                                      * (internalDocument.length()
                                                          / averageDocumentLength)))))
                      .average()
                      .orElse(0d);
              return new Result<>(internalDocument.document, score);
            })
        .distinct()
        .filter(result -> !(result.score() <= 0.1d))
        .sorted(Comparator.comparingDouble(Result<T>::score).reversed())
        .collect(Collectors.toList());
  }

  public void clear() {
    this.indexStorage.clear();
  }
}
