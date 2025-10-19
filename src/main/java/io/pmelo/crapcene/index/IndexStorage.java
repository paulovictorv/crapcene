package io.pmelo.crapcene.index;

import java.util.List;
import java.util.Set;

public interface IndexStorage<T> {
  int size();

  void add(Set<String> term, InternalDocument<T> document);

  List<InternalDocument<T>> documents();

  Set<InternalDocument<T>> documentsWithTerm(String term);

  void close();

  void clear();
}
