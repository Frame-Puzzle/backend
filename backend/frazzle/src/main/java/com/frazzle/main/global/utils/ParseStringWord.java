package com.frazzle.main.global.utils;

import java.util.Arrays;

public class ParseStringWord {

    /*
    #단어1#단어2#단어3 등의 단어를
    단어1, 단어2, 단어3 으로 분할한다.
     */
    public static String[] hashTagToStringToken(String words){

        String[] resultStr = Arrays.stream(words.split("#"))
                        .filter(s-> !s.isEmpty())
                                .toArray(String[]::new);

        return resultStr;
    }
    /*
    단어1, 단어2, 단어3 등의 문자열 배열을
    #단어1#단어2#단어3 로 변경한다.
     */
    public static String StringToHashTag(String[] words){
        String result = "";

        if(words == null || words.length == 0)
            return null;

        for(String word : words){
            result += "#"+word;
        }

        return result;
    }
}
