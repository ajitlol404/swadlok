package com.swadlok.dto;

import java.util.List;

public record DataTableResponse<T>(
        int draw,
        long recordsTotal,
        long recordsFiltered,
        List<T> data
) {}
