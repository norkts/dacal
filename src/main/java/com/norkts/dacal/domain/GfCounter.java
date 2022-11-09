package com.norkts.dacal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GfCounter {
    private AtomicInteger g2Counter = new AtomicInteger(0);
    private AtomicInteger g5Counter = new AtomicInteger(0);
    private AtomicInteger g10Counter = new AtomicInteger(0);
    private AtomicInteger g100Counter = new AtomicInteger(0);

    @Override
    public String toString(){
        return g100Counter.get()+"-" + g10Counter.get() + "-" + g5Counter.get() + "-" + g2Counter.get();
    }

    public void clearG2510(){
        g2Counter.set(0);
        g5Counter.set(0);
        g10Counter.set(0);
    }

    public void clearAll(){
        clearG2510();
        g100Counter.set(0);
    }
}
