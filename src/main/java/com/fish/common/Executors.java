package com.fish.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: fjjdragon
 * @date: 2021-03-22 0:22
 */
public class Executors {

//    private final static java.util.concurrent.ExecutorService executorService = new ThreadPoolExecutor(1,
//            Runtime.getRuntime().availableProcessors() * 4, 60L, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<>());

    private final static java.util.concurrent.ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 4, 30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new ThreadFactoryBuilder().setNameFormat("Executors-pool-runner-%d").build());

    public static void execute(Runnable task) {
        executorService.execute(task);
    }

}