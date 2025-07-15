package com.sertifikasi.bukuku.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private int yearPublished;
    private String genre;
    private String status;

    public Book() {}

    public Book(String title, String author, int yearPublished, String genre, String status) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.status = status;
    }

    public Book(int id, String title, String author, int yearPublished, String genre, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYearPublished() { return yearPublished; }
    public String getGenre() { return genre; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYearPublished(int yearPublished) { this.yearPublished = yearPublished; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setStatus(String status) { this.status = status; }
}
