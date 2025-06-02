package org.skypro.counter.model.search;

import java.util.UUID;

public interface Searchable {
    UUID getId();
    String getSearchTerm();
    String getContentType();
    String getName();
}