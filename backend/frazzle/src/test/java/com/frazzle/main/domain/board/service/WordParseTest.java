package com.frazzle.main.domain.board.service;

import com.frazzle.main.global.utils.ParseStringWord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WordParseTest {

    private String missions = "#성심당에서 사진 찍기#단체로 볼하트 만들기#호두아몬드율무차 들고 촬영";
    private String keywords = "#대전#호두#여행";

    @Test
    void 키워드_및_미션_파싱테스트(){
        List<String> splitMission = ParseStringWord.hashTagToStringToken(missions);
        List<String> splitKeyword = ParseStringWord.hashTagToStringToken(keywords);

        for (String s : splitMission)
            System.out.println(s);

        System.out.println();

        for (String s : splitKeyword)
            System.out.println(s);

        String fullMission = ParseStringWord.StringToHashTag(splitMission);
        String fullKeyword = ParseStringWord.StringToHashTag(splitKeyword);

        System.out.println();
        System.out.println(fullMission);
        System.out.println(fullKeyword);
    }
}
