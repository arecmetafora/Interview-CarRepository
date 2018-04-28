package com.arecmetafora.interview.carrepository.api;

import java.util.LinkedHashMap;

/**
 * POJO for all classes returned by the service REST API.
 *
 */
public class ApiResponse {

    /**
     * Number of the page request.
     */
    public int page;

    /**
     * Maximum of items for the page request.
     */
    public int pageSize;

    /**
     * Total of pages the whole request contains.
     */
    public int totalPageCount;

    /**
     * List of objects returned for the page request.
     */
    public LinkedHashMap<String, String> wkda;
}
