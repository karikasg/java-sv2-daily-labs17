package day01;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String title;
    private LocalDate localDate;
    private double rate;

    public Movie(Long id, String title, LocalDate localDate) {
        this.id = id;
        this.title = title;
        this.localDate = localDate;
    }

    public double getRate() {
        return rate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", localDate=" + localDate +
                "}\n";
    }
}
