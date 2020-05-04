package wolox.bootstrap.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageWrapper<T> implements Page<T> {

    Page<T> page;

    public PageWrapper(Page<T> page) {
        this.page = page;
    }

    @Override
    @JsonIgnore
    public Pageable getPageable() {
        return page.getPageable();
    }

    @Override
    @JsonProperty("total_pages")
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    @JsonProperty("total_count")
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    @JsonProperty("current_page")
    public int getNumber() {
        return page.getNumber() + 1;
    }

    @JsonProperty("previous_page")
    public Integer getNumberPreviousPage() {
        return page.hasPrevious() ? getNumber() - 1 : null;
    }

    @JsonProperty("next_page")
    public Integer getNumberNextPage() {
        return page.hasNext() ? getNumber() + 1 : null;
    }

    @Override
    @JsonProperty("limit")
    public int getSize() {
        return page.getSize();
    }

    @Override
    @JsonProperty("count")
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @Override
    @JsonProperty("page")
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @Override
    @JsonIgnore
    public Sort getSort() {
        return page.getSort();
    }

    @Override
    @JsonIgnore
    public boolean isFirst() {
        return page.isFirst();
    }

    @Override
    @JsonIgnore
    public boolean isLast() {
        return page.isLast();
    }

    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return page.map(converter);
    }

    @Override
    public Iterator<T> iterator() {
        return page.iterator();
    }
}
