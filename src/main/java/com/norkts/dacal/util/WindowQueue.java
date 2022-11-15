package com.norkts.dacal.util;


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

    private int capacity;
    public WindowQueue(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    @Override
    public boolean add(T t){
        if(size() >= capacity){
            poll();
        }
        return super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> items){
        for(T item : items){
            add(item);
        }
        return true;
    }

    public List<T> getItemsAsList(){

        return new ArrayList<>(this);
    }
}

