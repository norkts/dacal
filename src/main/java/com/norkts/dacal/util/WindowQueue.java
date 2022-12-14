package com.norkts.dacal.util;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 定长队列
 * @param <T>
 */
public class WindowQueue<T> extends ArrayBlockingQueue<T> {
    private static final long serialVersionUID = -1818403798613768240L;

    @Getter
    @Setter
    private int capacity;

    @Getter
    @Setter
    private List<T> items = Lists.newArrayList();

    public WindowQueue(){
        this(16);
    }

    public WindowQueue(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    @Override
    public boolean add(T t){
        if(size() >= capacity){
            poll();
        }
        boolean res = super.add(t);

        items = new ArrayList<>(this);
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends T> items){
        for(T item : items){
            add(item);
        }
        return true;
    }
}

