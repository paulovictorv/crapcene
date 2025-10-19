package io.pmelo.crapcene;

public record Result<T>(Document<T> document, double score) implements Comparable<Result<T>> {
  @Override
  public int compareTo(Result o) {
    return Double.compare(o.score, this.score);
  }
}
