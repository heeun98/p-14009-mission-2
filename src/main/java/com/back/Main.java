package com.back;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static Scanner sc;
    public static int lastIdx = 1;

    public static List<WiseSaying> sayings = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        sc = new Scanner(System.in);

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String operation = sc.nextLine().trim();

            if (operation.equals("종료")) {//완성
                return;

            } else if (operation.equals("등록")) {//완성

                WiseSaying quote = create();
                JsonFileSaver.save(quote);

            } else if (operation.equals("목록")) {
                //완성
                System.out.println("sayings.toString() = " + sayings.toString());
                showList();

            } else if (operation.contains("삭제")) {//완성

                int deleteId = getRealNumber(operation);
                delete(deleteId);

            } else if (operation.contains("수정")) {//진행중

                int updateId = getRealNumber(operation);
                JsonFileSaver.update(sayings.get(updateId - 1), sayings);

            } else if (operation.equals("빌드")) {

                Path dir = Paths.get("db", "wiseSaying");
                JsonAggregator.aggregateToArray(dir);
            }
        }
    }

    private static Integer getRealNumber(String operation) {
        String number = operation.substring(6);
        return Integer.parseInt(number);
    }



    //완성
    private static void delete(int deleteId) throws IOException {

        sayings.remove(deleteId - 1);

        Path file = Paths.get("db", "wiseSaying", deleteId + ".json");
        // 파일이 존재하면 삭제
        boolean b = Files.deleteIfExists(file);

        if (b) {
            System.out.println(deleteId + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(deleteId + "번 명언이 존재하지않습니다.");
        }

        System.out.println("lastId.txt 파일을 삭제했습니다: " + file.toAbsolutePath());

    }

    //완성
    private static WiseSaying create() throws IOException {

        System.out.print("명언 :");
        String title = sc.nextLine().trim();

        System.out.print("작가 :");
        String author = sc.nextLine().trim();

        WiseSaying wiseSaying = new WiseSaying(lastIdx, title, author);
        sayings.add(wiseSaying);

        System.out.println(lastIdx + "번 명언이 등록되었습니다.");

        //lastId.txt 생서하기

        Path lastDir = Paths.get("db", "wiseSaying");

        Path dir = Paths.get("db", "wiseSaying");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("디렉토리 생성 실패: " + e.getMessage());
        }

        Path file = dir.resolve( "lastId" + ".txt");

        Files.writeString(
                file,
                String.valueOf(lastIdx),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,            // 파일이 없으면 생성
                StandardOpenOption.TRUNCATE_EXISTING  // 파일이 있으면 덮어쓰기
        );


        lastIdx++;

        return wiseSaying;
    }



    private static void showList() throws IOException {

        Path dir = Paths.get("db", "wiseSaying");


        DirectoryStream<Path> stream =
                Files.newDirectoryStream(dir, "*.json");

        List<WiseSaying> quotes = new ArrayList<>();

        for (Path path : stream) {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            quotes.add(parseQuote(json));
        }

        // 3) id 내림차순 정렬
        quotes.sort(Comparator.comparingInt(WiseSaying::getId).reversed());

        // 4) 출력

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying q : quotes) {
            System.out.printf("%d / %s / %s%n",
                    q.getId(), q.getAuthor(), q.getText());
        }



    }


    // JSON 문자열에서 id, content, author를 간단히 꺼내서 Quote 생성
    private static WiseSaying parseQuote(String json) {
        int id = extractInt(json, "\"id\"\\s*:\\s*(\\d+)");
        String content = extractString(json, "\"content\"\\s*:\\s*\"([^\"]*)\"");
        String author  = extractString(json, "\"author\"\\s*:\\s*\"([^\"]*)\"");
        return new WiseSaying(id, content, author);
    }

    private static int extractInt(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private static String extractString(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(1) : "";
    }


    private static void testRq1() {
        Rq rq = new Rq("목록?searchKeywordType=content&searchKeyword=자바");
        String searchKeyword = rq.getParam("searchKeyword", "");

        System.out.println("searchKeyword = " + searchKeyword);
    }


}