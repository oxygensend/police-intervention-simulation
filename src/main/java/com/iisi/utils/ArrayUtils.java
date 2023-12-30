package com.iisi.utils;

public class ArrayUtils {
    private ArrayUtils() {
    }

    public static int sumArray(int[] array) {
        int sum = 0;
        for (int j : array) {
            sum += j;
        }

        return sum;

    }

}