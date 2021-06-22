package com.fasterxml.jackson.dataformat.ron.databind;

/**
 * Example POJO based on https://schema.org/Book
 */
class Book {

    private boolean abridged;
    private int numberOfPages;

    Book(boolean abridged, int numberOfPages) {
        this.abridged = abridged;
        this.numberOfPages = numberOfPages;
    }

    public boolean isAbridged() {
        return abridged;
    }

    public void setAbridged(boolean abridged) {
        this.abridged = abridged;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
