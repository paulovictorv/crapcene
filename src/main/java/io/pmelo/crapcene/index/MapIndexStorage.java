package io.pmelo.crapcene.index;

import java.util.*;

public class MapIndexStorage<T> implements IndexStorage<T> {

  private final Map<String, List<InternalDocument<T>>> inverseIndexMap;

  public MapIndexStorage() {
    this.inverseIndexMap = new HashMap<>();
  }

  @Override
  public int size() {
    return this.inverseIndexMap.values().stream().mapToInt(List::size).sum();
  }

  @Override
  public List<InternalDocument<T>> documents() {
    return this.inverseIndexMap.values().stream().flatMap(Collection::stream).toList();
  }

  @Override
  public Set<InternalDocument<T>> documentsWithTerm(String term) {
    return Set.of();
  }

  @Override
  public void close() {}

  @Override
  public void clear() {
    inverseIndexMap.clear();
  }

  @Override
  public void add(Set<String> terms, InternalDocument<T> document) {
    terms.forEach(
        term ->
            inverseIndexMap.compute(
                term,
                (key, list) -> {
                  if (list == null) {
                    list = new ArrayList<>();
                  }
                  list.add(document);
                  return list;
                }));
  }
}
