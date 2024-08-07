package com.frazzle.main.global.utils;

import java.security.SecureRandom;

public class GenerateRandomNickname {

    //초기 닉네임 설정을 위한 랜덤 닉네임 생성기
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int NICKNAMELENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    //10글자인 랜덤 닉네임 생성하기
    public static String generateRandomNickname() {
        StringBuilder nickname = new StringBuilder(NICKNAMELENGTH);
        for (int i = 0; i < NICKNAMELENGTH; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            nickname.append(ALPHABET.charAt(index));
        }
        return nickname.toString();
    }

    public static SecureRandom getRandom(){
        return RANDOM;
    }
}
