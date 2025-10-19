package io.pmelo.crapcene.index;

import java.util.*;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

public class MVStoreIndexStorage<T> implements IndexStorage<T> {

  private final MVStore store;
  private final MVMap<String, List<InternalDocument<T>>> inverseIndex;

  public MVStoreIndexStorage() {
    store = MVStore.open("index.db");
    inverseIndex = store.openMap("inverseIndex");
  }

  @Override
  public int size() {
    return inverseIndex.size();
  }

  @Override
  public List<InternalDocument<T>> documents() {
    return inverseIndex.values().stream().flatMap(Collection::stream).toList();
  }

  @Override
  public Set<InternalDocument<T>> documentsWithTerm(String term) {
    return new HashSet<>(inverseIndex.get(term));
  }

  @Override
  public void add(Set<String> terms, InternalDocument<T> document) {
    terms.parallelStream()
        .forEach(
            term ->
                inverseIndex.compute(
                    term,
                    (key, list) -> {
                      if (list == null) {
                        list = new ArrayList<>();
                      }
                      list.add(document);
                      return list;
                    }));
  }

  @Override
  public void close() {
    store.commit();
    store.close();
  }

  @Override
  public void clear() {
    inverseIndex.clear();
    store.commit();
  }
}
