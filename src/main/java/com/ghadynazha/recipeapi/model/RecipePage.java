package com.ghadynazha.recipeapi.model;

import java.util.List;

/**
 * Represents a paginated result of Recipe objects.
 * Contains metadata such as total pages, total elements, and current page number.
 *
 * Author: Ghady Nazha
 */
public class RecipePage {
    /**
     * The list of recipes in the current page.
     */
    private List<Recipe> content;

    /**
     * The total number of pages available.
     */
    private int totalPages;

    /**
     * The total number of recipe entries.
     */
    private long totalElements;

    /**
     * The current page number (0-based index).
     */
    private int number;

    /**
     * Default constructor for RecipePage.
     */
    public RecipePage() {}

    /**
     * Gets the list of recipes in the current page.
     * @return list of Recipe objects
     */
    public List<Recipe> getContent() {
        return content;
    }

    /**
     * Sets the list of recipes in the current page.
     * @param content list of Recipe objects
     */
    public void setContent(List<Recipe> content) {
        this.content = content;
    }

    /**
     * Gets the total number of pages available.
     * @return total pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total number of pages available.
     * @param totalPages total pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the total number of recipe entries.
     * @return total elements
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Sets the total number of recipe entries.
     * @param totalElements total elements
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets the current page number (0-based index).
     * @return page number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the current page number (0-based index).
     * @param number page number
     */
    public void setNumber(int number) {
        this.number = number;
    }
}