package com.noah.starter.mysql.offset;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.ToString;

import java.util.List;
import java.util.function.Function;

/**
 * 数据库使用offset而不是currentPage的翻页
 **/
@ToString
public class NoahPage<T> implements IPage<T> {
    /**
     *
     */
    private static final long serialVersionUID = -2332884250408355301L;

    private long offset = 0;
    private long count = 20;
    private List<T> records;
    private long total = 0;
    private boolean searchCount = false;

    public NoahPage(long offset, long count) {
        this.offset = offset;
        this.count = count;
    }

    public NoahPage(long offset, long count, List<T> records, long total) {
        this.offset = offset;
        this.count = count;
        this.records = records;
        this.total = total;
    }

    public <K> NoahPage(NoahPage<K> page, Function<List<K>, List<T>> convert) {
        this(page.offset, page.count, convert.apply(page.records), page.total);
    }

    @Override
    public List<OrderItem> orders() {
        return null;
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return count;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.count = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return offset / count + 1;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.offset = (current - 1) * count;
        return this;
    }

    @Override
    public long offset() {
        return offset;
    }

    @Override
    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }
}
