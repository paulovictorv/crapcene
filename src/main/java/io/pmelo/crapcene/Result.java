package io.pmelo.crapcene;

public record Result(Document document, double score) implements Comparable<Result> {
    @Override
    public int compareTo(Result o) {
        return Double.compare(o.score, this.score);
    }
}
