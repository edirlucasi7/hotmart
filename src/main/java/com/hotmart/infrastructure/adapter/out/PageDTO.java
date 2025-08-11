package com.hotmart.infrastructure.adapter.out;

import com.hotmart.application.port.PagePort;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PageDTO<T> implements PagePort<T> {

    private final Page<T> page;

    public PageDTO(Page<T> page) {
        this.page = page;
    }

    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public int getCurrentPage() {
        return page.getNumber();
    }

    @Override
    public int getElementsPerPage() {
        return page.getSize();
    }

    @Override
    public <U> PagePort<U> map(Function<? super T, ? extends U> converter) {
        return new PageDTO<>(page.map(converter));
    }
}
