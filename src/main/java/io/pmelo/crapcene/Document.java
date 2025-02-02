package io.pmelo.crapcene;

import lombok.Builder;


@Builder(toBuilder = true)
public record Document<T>(String id, String content, T value) {
}

