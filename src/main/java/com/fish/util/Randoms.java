package com.fish.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: fjjdragon
 * @date: 2021-04-24 15:19
 */
public class Randoms {

    public static final int RANDOM_BASE = 10000;

    private static Random random() {
        return ThreadLocalRandom.current();
    }

    public static int from(int start, int end) {
        Preconditions.checkArgument(start <= end, "Start is larger than end");
        if (start == end) {
            return start;
        }
        return random().nextInt(end - start + 1) + start;
    }

    public static boolean happen(String percent) {
        int rate = percentToInt(percent);
        return from(1, RANDOM_BASE) <= rate;
    }

    private static int percentToInt(String percent) {
        Preconditions.checkArgument(percent.charAt(percent.length() - 1) == '%', "Not a percent string");
        float p = Float.parseFloat(percent.substring(0, percent.length() - 1));
        return (int) (p / 100 * RANDOM_BASE);
    }

    public static <T> T from(List<T> list) {
        if (list == null || list.isEmpty())
            return null;
        return list.get(from(0, list.size() - 1));
    }

    public static <T> T fromExcludeT(List<T> list, List<T> exclude) {
        if (list == null || list.isEmpty())
            return null;
        HashSet<T> set = new HashSet<>(list);
        for (T item : exclude) {
            if (set.contains(item)) {
                set.remove(item);
            }
        }
        Object[] objects = set.toArray();

        return (T) objects[from(0, objects.length - 1)];
    }

    public static <T> T from(Collection<T> collect) {
        if (collect == null || collect.isEmpty()) {
            return null;
        }
        List<T> list = Lists.newArrayList(collect);
        return from(list);
    }

    public static <T> T from(Map<T, Integer> randomMap) {
        if (randomMap == null || randomMap.isEmpty()) {
            return null;
        }
        int totalRate = 0;
        for (Map.Entry<T, Integer> entry : randomMap.entrySet()) {
            totalRate += entry.getValue();
            if (totalRate > RANDOM_BASE) {
                break;
            }
        }
        if (totalRate > RANDOM_BASE) {
            randomMap = adjustRandomMap(randomMap, RANDOM_BASE);
        }
        return trulyFrom(randomMap);
    }

    public static <T> T fromWeight(Map<T, Integer> weightMap) {
        Preconditions.checkNotNull(weightMap);
        int totalRate = 0;
        for (int rate : weightMap.values()) {
            totalRate += rate;
        }
        if (totalRate != RANDOM_BASE) {
            weightMap = adjustRandomMap(weightMap, RANDOM_BASE);
        }
        return trulyFrom(weightMap);
    }

    public static <T> T fromWeightExcludeT(final Map<T, Integer> weightMap, List<T> exclude) {
        Preconditions.checkNotNull(weightMap);
        if (null == exclude || exclude.size() == 0) {
            return fromWeight(weightMap);
        }
        Map<T, Integer> hashMap = new HashMap<>(weightMap);
        for (T item : exclude) {
            if (null != hashMap.get(item)) {
                hashMap.remove(item);
            }
        }
        int totalRate = 0;
        for (int rate : hashMap.values()) {
            totalRate += rate;
        }
        if (totalRate != RANDOM_BASE) {
            hashMap = adjustRandomMap(hashMap, RANDOM_BASE);
        }
        return trulyFrom(hashMap);
    }

    private static <T> T trulyFrom(Map<T, Integer> randomMap) {
        int v = from(1, RANDOM_BASE);
        int beginIndex = 0;
        for (Map.Entry<T, Integer> entry : randomMap.entrySet()) {
            if (v > beginIndex && v <= beginIndex + entry.getValue()) {
                return entry.getKey();
            } else {
                beginIndex += entry.getValue();
            }
        }
        return null;
    }

    private static <T> Map<T, Integer> adjustRandomMap(Map<T, Integer> randomMap, int total) {
        float currentTotalRate = 0;
        Map<T, Integer> newMap = Maps.newHashMap();
        for (Map.Entry<T, Integer> entry : randomMap.entrySet()) {
            currentTotalRate += entry.getValue();
        }
        int index = 0;
        int lastIndex = randomMap.size() - 1;
        int totalAlloc = 0;
        float factor = total / currentTotalRate;
        for (Map.Entry<T, Integer> entry : randomMap.entrySet()) {
            if (index == lastIndex) {
                newMap.put(entry.getKey(), Math.max(total - totalAlloc, 0));
            } else {
                int alloc = Math.round(entry.getValue() * factor);
                totalAlloc += alloc;
                newMap.put(entry.getKey(), alloc);
            }
            index++;
        }
        return newMap;
    }

}