package io.pmelo;

import io.pmelo.crapcene.Document;
import io.pmelo.crapcene.Result;
import io.pmelo.crapcene.index.Index;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource("entries.txt");
        assert resource != null;
        List<String> entries = Files.readAllLines(Path.of(resource.toURI()));
        Map<String, Document> descriptions = entries.stream()
                .map(entry -> Document.builder()
                        .id(UUID.randomUUID().toString())
                        .content(entry)
                        .build()
                )
                .collect(Collectors.toMap(Document::id, Function.identity()));

        Index index = new Index();
        descriptions.values().forEach(index::addDocument);
        List<Result> results = index.query("pix claro");
        results.forEach(System.out::println);
    }
}