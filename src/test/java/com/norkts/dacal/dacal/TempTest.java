package com.norkts.dacal.dacal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Lists;
import com.norkts.dacal.domain.GamblingData;
import com.norkts.dacal.domain.GiftType;
import com.norkts.dacal.util.WindowQueue;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempTest {
    @Test
    public void genJson() throws IOException, ClassNotFoundException {
        Kryo kryo = new Kryo();

        kryo.register(WindowQueue.class);

        WindowQueue<String> windowQueue = new WindowQueue<>(5);
        windowQueue.add("1");
        windowQueue.add("2");
        windowQueue.add("3");
        windowQueue.add("4");
        windowQueue.add("5");
        windowQueue.add("6");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, windowQueue);
        output.close();

        long start = System.currentTimeMillis();
        Input input = new Input(new ByteArrayInputStream(baos.toByteArray()));
        WindowQueue<String> windowQueue2 = kryo.readObject(input, WindowQueue.class);
        input.close();
        windowQueue2.add("7");
        System.out.println(windowQueue2 + ",cost=" + (System.currentTimeMillis() - start) + ",size=" + baos.toByteArray().length);


        baos = new ByteArrayOutputStream();
        ObjectOutputStream ots = new ObjectOutputStream(baos);
        ots.writeObject(windowQueue);
        ots.close();

        start = System.currentTimeMillis();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        windowQueue2 = (WindowQueue<String>) ois.readObject();
        windowQueue2.add("7");

        System.out.println(windowQueue2 + ",cost=" + (System.currentTimeMillis() - start) + ",size=" + baos.toByteArray().length);
    }
}
