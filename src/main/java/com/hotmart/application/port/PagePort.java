package com.hotmart.application.port;

import java.util.List;
import java.util.function.Function;

public interface PagePort<T> {

    List<T> getContent();

    int getTotalPages();

    long getTotalElements();

    int getCurrentPage();

    int getElementsPerPage();

    <U> PagePort<U> map(Function<? super T, ? extends U> converter);
}
