package io.pmelo.crapcene.index;

import io.pmelo.crapcene.Document;
import lombok.Builder;

import java.util.Map;

@Builder
final class InternalDocument {
    Document document;
    Map<String, Integer> termCount;

    public int termCount(String term) {
        return termCount.getOrDefault(term, 0);
    }

    public int length() {
        return termCount.size();
    }
}
