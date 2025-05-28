package com.back;

import java.util.Objects;

public class WiseSaying {

    private int id;
    private String text;
    private String author;

    public WiseSaying(int id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WiseSaying quote = (WiseSaying) o;
        return Objects.equals(text, quote.text) && Objects.equals(author, quote.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, author);
    }
}
