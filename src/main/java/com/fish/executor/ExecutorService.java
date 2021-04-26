package com.fish.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: fjjdragon
 * @date: 2021-03-22 0:22
 */
public class ExecutorService {

    private final static java.util.concurrent.ExecutorService executorService = new ThreadPoolExecutor(1,
            Runtime.getRuntime().availableProcessors() * 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    public static void execute(Runnable task) {
        executorService.execute(task);
    }

}