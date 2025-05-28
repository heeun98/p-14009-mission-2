package com.back;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class JsonFileSaver {

    public static void update(WiseSaying quote, List<WiseSaying> sayings) throws IOException {

        Scanner sc = new Scanner(System.in);

        int updateId = quote.getId();

        System.out.println("명언(기존) : " + quote.getText());
        System.out.print("명언 : ");
        String newText = sc.nextLine().trim();
        System.out.println("작가(기존) : " + quote.getText());
        System.out.print("작가 :");
        String newAuthor = sc.nextLine().trim();

        sayings.set(updateId - 1, new WiseSaying(updateId, newText, newAuthor));

        // 1) 파일 경로 생성
        Path file = Paths.get("db", "wiseSaying", updateId + ".json");

        // 2) 해당 파일이 없으면 에러
        if (Files.notExists(file)) {
            throw new IOException("수정할 파일이 없습니다: " + file.toAbsolutePath());
        }

        // 3) 새 JSON 문자열 생성 (pretty print)
        String json =
                "{\n" +
                        "  \"id\": " + updateId + ",\n" +
                        "  \"content\": \"" + escapeJson(newText) + "\",\n" +
                        "  \"author\": \""  + escapeJson(newAuthor)  + "\"\n" +
                        "}";

        // 4) TRUNCATE_EXISTING 옵션으로 기존 내용 덮어쓰기
        Files.writeString(
                file,
                json,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING
        );


    }


    public static void save(WiseSaying quote) {

        int id = quote.getId();
        String text = quote.getText();
        String author = quote.getAuthor();

        String json = "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"content\": \"" + escapeJson(text) + "\",\n" +
                "  \"author\": \"" + escapeJson(author) + "\"\n" +
                "}";

        // 2) 디렉토리 생성
        Path dir = Paths.get("db", "wiseSaying");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("디렉토리 생성 실패: " + e.getMessage());
        }


        // 3) 파일에 쓰기
        Path file = dir.resolve(id + ".json");
        try {
            Files.write(file, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("파일 쓰기 실패: " + e.getMessage());
        }


    }

    // JSON에 포함할 문자열에 " 또는 \ 가 있으면 이스케이프해 줍니다.
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

}
