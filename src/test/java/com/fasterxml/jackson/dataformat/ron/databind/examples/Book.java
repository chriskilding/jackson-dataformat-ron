package com.fasterxml.jackson.dataformat.ron.databind.examples;

import java.util.Objects;

/**
 * Example POJO based on https://schema.org/Book
 */
public class Book {

    public boolean abridged;
    public int numberOfPages;

    public Book() {
        // no-op
    }

    public Book(boolean abridged, int numberOfPages) {
        this.abridged = abridged;
        this.numberOfPages = numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return abridged == book.abridged && numberOfPages == book.numberOfPages;
    }

    @Override
    public int hashCode() {
        return Objects.hash(abridged, numberOfPages);
    }
}
