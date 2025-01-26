# Crapcene
_Lucene, but crappier_

## What is this?
This is a small implementation of an information retrieval system. It is built around the statistical model called BM25,
which is one strategy to calculate a document score in regard to a query. The formula was implemented verbatim from what
is specified in this Wikipedia Article.

```java

 inverseIndexMap.values().stream()
                .flatMap(Collection::stream)
                .map(internalDocument -> {
                    double score = tokenizer.apply(query)
                            .mapToDouble(queryTerm -> this.inverseDocumentFrequency.apply(queryTerm) *
                                    (
                                            (internalDocument.termCount(queryTerm) * (K + 1)) /
                                                    (internalDocument.termCount(queryTerm) + K * (1 - B + B * (internalDocument.length() / averageDocumentLength))))
                            )
                            .average()
                            .orElse(0d);
                    return new Result(internalDocument.document, score);
                })
                .distinct()
                .filter(result -> !(result.score() <= 0.1d))
                .sorted(Comparator.comparingDouble(Result::score).reversed())
                .collect(Collectors.toList());

```

## How it works

You first need to index your documents. This library will then build an in-memory reverse index, which ties _terms_ to
a list of potential documents. 

```java
        Index index = new Index();            
            index.addDocument(
                    Document.builder()
                            .id(UUID.randomUUID().toString())
                            .content("a content")
                            .build()
            );
```

After that, you can query your index by any term you want:

```java
content = index.query("content");
```

It will return you a list of `Result`, which is a container object for Document, score tuple. This list is always sorted
by score, descending. So if you want to find the most relevant match, getting the first result should be enough.

## Disclaimer
This is by no means a production ready lib. It is fine for small projects where you might want to have _some_ searching
and classifying, but it will definitely break for larger corpus.

## Next steps
1. Make it available in maven central.
2. Refactor the average calculation to be a cumulative average instead of recalculating the full average everytime
3. Add configuration parameters to allow tuning of BM25 constants (K and B)
4. Abstract away the tokenizer. Right now it is splitting words with white spaces, but a more fancy tokenizer can be implemented to ignore stopwords (for example)
5. Abstract away the storage mechanism, allowing for having a not in-memory implementation i.e. a database or a file.