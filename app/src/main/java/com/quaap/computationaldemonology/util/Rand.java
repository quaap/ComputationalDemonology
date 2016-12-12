package com.quaap.computationaldemonology.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by tom on 12/11/16.
 */

public class Rand {


    private static Random rand = new Random();



    /**
     * Get a random integer
     *
     * @return a random integer
     */
    public static int getInt() {
        return rand.nextInt();
    }

    /**
     * Get a random integer
     *
     * @return a random integer
     */
    public static int getInt(int ceiling) {
        return rand.nextInt(ceiling);
    }

    /**
     * Get a random integer
     *
     * @return a random integer
     */
    public static int getInt(int floor, int ceiling) {
        return rand.nextInt(ceiling-floor) + floor;
    }
    /**
     * Get a random integer between 0 and max, inclusive
     *
     * @param max the maximum number (inclusive)
     * @return a random integer in the specified range
     */
    public static int getNumber(int max) {
        return rand.nextInt(max+1);
    }

    /**
     * Get a random integer between min and max, inclusive
     *
     * @param min the minimum number (inclusive)
     * @param max the maximum number (inclusive)
     * @return a random integer in the specified range
     */
    public static int getNumber(int min, int max) {
        return rand.nextInt(max-min + 1) + min;
    }

    /**
     * Get a random integer between 1 and 6, inclusive
     */
    public static int diceRoll() {
        return diceRoll(6);
    }

    /**
     * Get a random integer between 1 and sides, inclusive
     * @return  a random integer in the specified range
     */
    public static int diceRoll(int sides) {
        return rand.nextInt(sides) + 1;
    }

    /**
     * Get a random double between 0 (inclusive) and 1 (exclusive)
     *
     * @return a random double in the specified range
     */
    public static double getDouble() {
        return rand.nextDouble();
    }

    /**
     * Get a random double between -1 (inclusive) and 1 (exclusive)
     *
     * @return a random double in the specified range
     */
    public static double getDoubleNeg1To1() {
        return (rand.nextDouble() - .5)*2;
    }

    /**
     * Get a random double between 0 (inclusive) and ceiling (exclusive)
     * @param ceiling the maximum value (exclusive).
     * @return a random double in the specified range
     */
    public static double getDouble(double ceiling) {
        return rand.nextDouble()*ceiling;
    }

    /**
     * Get a random double between floor (inclusive) and ceiling (exclusive)
     *
     * @param floor  the minumum value (inclusive). Must be less than ceiling
     * @param ceiling the maximum value (exclusive).
     * @return a random double in the specified range
     */
    public static double getDouble(double floor, double ceiling) {
        return (rand.nextDouble()*(ceiling-floor)) + floor;
    }

    /**
     * get true or false, randomly
     *
     * @return true or false
     */
    public static boolean getBoolean() {
        return rand.nextBoolean();
    }

    /**
     * For use when you want a condition to happen a certain percentage of the time
     *
     * @param percentchance the percent chance (0-100) the method should return true
     * @return true if the event should happen
     */
    public static boolean chance(double percentchance) {
        return rand.nextDouble() < percentchance/100.0;
    }


    public static int rand(int ... members) {
        return members[getInt(members.length)];
    }

    public static <T> T rand(T ... members) {
        return members[getInt(members.length)];
    }

    public static <T> T rand(Collection<T> members) {
        return new RandomList<>(members).rand();
    }

    public static <T> T rand(List<T> members) {
        return members.get(getInt(members.size()));
    }

    public static String getSubstr(final String str, int length) {
        int strlen = str.length();
        length = Math.min(strlen, length);
        int rnd = getInt(strlen-length);
        return str.substring(rnd, rnd + length);
    }

    public static String getSubstr(final String str, int floor, int ceiling) {
        return getSubstr(str, getInt(floor,ceiling));
    }

    public static String getChar(final String str) {
        return getSubstr(str, 1);
    }

    /**
     * re-randomize the random instance
     */
    public static void randomize() {
        rand.setSeed(System.nanoTime());
    }

    ////////////////////////////

    interface GetRandom<T> {
        T rand();
    }


    public static class RandomList<T> extends ArrayList<T> implements GetRandom<T> {


        public RandomList(Collection<T> members) {
            this.addAll(members);
        }

        @SafeVarargs
        public RandomList(T ... members) {
            this.addAll(Arrays.asList(members));
        }

        public T rand() {
            return this.get(rand.nextInt(this.size()));
        }
    }

    public static class CharRange  implements GetRandom<String>{

        private final int mLowChar;
        private final int mHighChar;

        public CharRange(int lowChar, int highChar) {
            mLowChar = lowChar;
            mHighChar = highChar;
        }

        public CharRange(char lowChar, char highChar) {
            mLowChar = lowChar;
            mHighChar = highChar;
        }

        public String getCharAt(int pos) {
            return convert(mLowChar + pos);
        }

        public String rand() {

            return convert(rand.nextInt(mHighChar-mLowChar) + mLowChar);
        }

        private String[] allChars = null;

        public String[] getChars() {
            if (allChars==null) {
                allChars = new String[mHighChar - mLowChar];
                for (int i = 0; i < allChars.length; i++) {
                    allChars[i] = convert(mLowChar + i);
                }
            }
            return allChars;
        }

        public static String convert(int charpoint) {
            return new String(Character.toChars(charpoint));
        }


    }
}
