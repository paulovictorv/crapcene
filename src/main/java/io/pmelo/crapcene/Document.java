package io.pmelo.crapcene;

import lombok.Builder;


@Builder(toBuilder = true)
public record Document(String id, String content) {
}

