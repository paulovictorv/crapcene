package io.pmelo.crapcene;

import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
public record Document<T>(String id, String content, T value) implements Serializable {}
