package io.pmelo.crapcene.index;

import io.pmelo.crapcene.Document;
import lombok.Builder;

import java.io.Serializable;
import java.util.Map;

@Builder
public final class InternalDocument<T> implements Serializable {
    Document<T> document;
    Map<String, Integer> termCount;

    public int termCount(String term) {
        return termCount.getOrDefault(term, 0);
    }

    public int length() {
        return termCount.size();
    }
}
