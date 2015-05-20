package com.henrik.advergame.utils;

import java.util.Random;

/**
 * Created by Henri on 10/12/2014.
 */
public class RandomUtils {

    private static Random rand = new Random();

    public static int randInt(int min, int max) {
        return rand.nextInt((max-min) + 1) + min;
    }

}
