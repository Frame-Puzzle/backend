package com.frazzle.main.global.utils;

import java.util.ArrayList;
import java.util.List;

public class ParseStringWord {

    /*
    #단어1#단어2#단어3 등의 단어를
    단어1, 단어2, 단어3 으로 분할한다.
     */
    public static List<String> hashTagToStringToken(String words){

        String[] resultStr = words.split("#");

        List<String> resultList = new ArrayList<String>();

        for (String s : resultStr) {
            if(s.equals(""))
                continue;

            resultList.add(s);
        }


        return resultList;
    }

    /*
    단어1, 단어2, 단어3 등의 문자열 배열을
    #단어1#단어2#단어3 로 변경한다.
     */
    public static String StringToHashTag(List<String> words){
        String result = "";

        for(String word : words){
            result += "#"+word;
        }

        return result;
    }
}
