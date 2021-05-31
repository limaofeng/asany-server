package cn.asany.security.core.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @description:
 * @author: Poison
 * @time: 2020/9/22 10:57 AM
 */
public class RandomWordGenerator {
    private char[] possiblesChars;
    private Random myRandom = new SecureRandom();

    public RandomWordGenerator(String acceptedChars) {
        this.possiblesChars = acceptedChars.toCharArray();
    }

    public String getWord(Integer length) {
        StringBuilder word = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            word.append(this.possiblesChars[this.myRandom.nextInt(this.possiblesChars.length)]);
        }

        return word.toString();
    }

}
