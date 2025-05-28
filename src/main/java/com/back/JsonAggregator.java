package com.back;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class JsonAggregator {


    public static void aggregateToArray(Path dir) throws IOException {

        if (Files.notExists(dir) || !Files.isDirectory(dir)) {
            throw new IOException("디렉토리가 없습니다: " + dir.toAbsolutePath());
        }

        List<String> elements = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path path : stream) {
                // data.json은 제외(이미 생성된 적이 있을 수 있으므로)
                if ("data.json".equals(path.getFileName().toString())) {
                    continue;
                }
                // 파일 전체 내용을 읽어서 JSON 객체 문자열로 취급
                String json = Files.readString(path, StandardCharsets.UTF_8).trim();
                elements.add(json);
            }
        }

        // JSON 배열 형태로 합치기
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i));
            if (i < elements.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");

        // 결과를 data.json으로 쓰기
        Path out = dir.resolve("data.json");
        Files.writeString(
            out,
            sb.toString(),
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );

    }

}
