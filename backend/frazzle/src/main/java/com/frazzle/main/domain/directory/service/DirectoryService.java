package com.frazzle.main.domain.directory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.*;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.gpt.dto.GuideRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private static final int MAX_TOKEN = 500;
    private static final int MAX_MISSION_NUMBER = 5;

    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.model}")
    private String gptModel;

    @Value("${gpt.api.url}")
    private String url;

    @Transactional
    public Directory createDirectory(UserPrincipal userPrincipal, CreateDirectoryRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 디렉토리 생성
        Directory directory = Directory.createDirectory(requestDto);
        directoryRepository.save(directory);

        //3. 유저-디렉토리 정보 저장
        UserDirectory userDirectory = UserDirectory.createUserDirectory(directory, user, true);
        userDirectoryRepository.save(userDirectory);

        return directory;
    }

    @Transactional
    public void updateDirectoryName(UserPrincipal userPrincipal, UpdateDirectoryNameRequestDto requestDto, int directoryId){
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );;
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
          throw new CustomException(ErrorCode.DENIED_UPDATE);
        }

        //3. 유저가 디렉토리에 가입되어 있으면 수정
        directory.changeDirectoryName(requestDto.getDirectoryName());
    }

    @Transactional
    public List<UserByEmailResponseDto> findUserByEmail(UserPrincipal userPrincipal, String email, int directoryId) {
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );;
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_FIND_MEMBER);
        }

        //3. 유저가 디렉토리게 가입되어 있으면 멤버 조회
        List<User> users = userRepository.findUsersByEmail(email, directory);

        List<UserByEmailResponseDto> response = new ArrayList<>();

        for(User u : users) {
            response.add(UserByEmailResponseDto.createFindUserByEmailResponseDto(u));
        }

        return response;
    }

    @Transactional
    public void inviteMember(UserPrincipal userPrincipal, InviteOrCancelMemberRequestDto requestDto, int directoryId) {
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_INVITE_MEMBER);
        }

        //3. 초대하려는 유저가 존재하지 않거나 디렉토리에 가입되어 있으면 에러
        User member = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_EXIST_USER));

        if(userDirectoryRepository.existsByUserAndDirectory(member, directory)) {
            throw new CustomException(ErrorCode.DUPLICATED_DIRECTORY_MEMBER);
        }

        //4. 멤버 초대
        userDirectoryRepository.save(UserDirectory.createUserDirectory(directory, member, false));
        directory.changePeopleNumber(1);
        /*
        fcm 코드
         */
    }

    @Transactional
    public void cancelMemberInvitation(UserPrincipal userPrincipal, InviteOrCancelMemberRequestDto requestDto, int directoryId) {
        //1. 유저 및 디렉토리 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );
        User member = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 멤버 초대 취소 권한 확인
        //초대하는 사람이 디렉토리에 가입된 멤버가 아니면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_CANCEL_MEMBER);
        }
        //초대 취소되는 사람이 디렉토리에 초대가 되었고 승인하기 전이 아니면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, member, false)){
            throw new CustomException(ErrorCode.DENIED_CANCEL_MEMBER);
        }

        //3. 초대 취소
        userDirectoryRepository.deleteByUserAndDirectory(member, directory);
        directory.changePeopleNumber(-1);
    }

    @Transactional
    public List<FindMyDirectoryResponseDto> findMyDirectory(UserPrincipal userPrincipal, String category){
        //1. 유저 인증
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 유저 디렉토리 조회
        List<Directory> directories = directoryRepository.findMyDirectory(user, category);
        List<FindMyDirectoryResponseDto> response = new ArrayList<>();

        for(Directory d : directories) {
            response.add(FindMyDirectoryResponseDto.createFindMyDirectoryResponseDto(d));
        }

        return response;
    }

    @Transactional
    public DetailDirectoryResponsetDto findDetailDirectory(UserPrincipal userPrincipal, int directoryId) {
        int userId = userPrincipal.getId();

        if(!userDirectoryRepository.existsByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(userId, directoryId, true)) {
            throw new CustomException(ErrorCode.DENIED_DIRECTORY);
        }

        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        List<User> members = userRepository.findDirectoryUsers(directory);
        List<MemberListDto> memberList = new ArrayList<>();
        for(User m : members) {
            memberList.add(MemberListDto.createMemberList(m));
        }

        List<Board> boards = boardRepository.findBoards(directoryId);
        List<BoardListDto> boardList = new ArrayList<>();
        for(Board b : boards) {
            boardList.add(BoardListDto.createBoardList(b));
        }

        boolean isCurrentBoard = boards.get(0).getClearType() == 0 ? true : false;

        DetailDirectoryResponsetDto detailDirectoryResponsetDto
                = DetailDirectoryResponsetDto.createDetailDirectoryRequestDto(
                directory, true, memberList, boardList
        );

        return detailDirectoryResponsetDto;
    }

    public String[] createGuideList(int directoryId, GuideRequestDto requestDto) {

        //디렉토리 존재x이면 에러 발생
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        //미션 최대 재생성 개수 5개로 제한
        if(requestDto.getPreMissionList().length>MAX_MISSION_NUMBER) {
            throw new CustomException(ErrorCode.MAX_GPT_REQUEST);
        }

        RestTemplate restTemplate = new RestTemplate();

        // 요청 본문 준비
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptModel); // 올바른 모델 이름 사용

        // messages 배열 구성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        String jsonDescription = "지금 들어온 답변을 할 때 글씨를 전부 붙여서 답변해줘 이를 테면 ~한 사진찍기, ~한 사진찍기 이런 것처럼 절대 출력해서 다시 보낼때 띄어쓰기 하지마 만약 출력되는 게 4개라면 1.~한 사진찍기 2.~한 사진찍기 3.~한 사진찍기 4.~한 사진찍기 이렇게 반드시 답변해줘" +
                "출력되는게 1개면 한문장만 필요해";


        systemMessage.put("description", jsonDescription);

        // 키워드 배열을 사용하여 프롬프트 문자열 생성
        StringBuilder promptBuilder = new StringBuilder();

        //키워드 합치기
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(", ", requestDto.getKeywordList()));

        //디렉토리의 카테고리 추가
        sb.append(", ").append(directory.getCategory());
        String combinedKeywords = sb.toString();
        String prompt = String.format("'%s'가 연관되는 [장소] 또는 [상황] 추천하는 사진 찍기. %d개만 추천 해줘.", combinedKeywords, requestDto.getNum());

        promptBuilder.append(prompt).append("\n");

        if(requestDto.getPreMissionList().length > 0) {
            String exceptMissions = String.join(", ", requestDto.getPreMissionList());
            promptBuilder.append(exceptMissions).append("이 미션은 제외하고 추천해줘");
        }


        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", promptBuilder.toString()); // 생성한 프롬프트 문자열 사용

        requestBody.put("messages", List.of(systemMessage, userMessage)); // 불변 리스트 사용
        requestBody.put("max_tokens", MAX_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();

                log.info(Objects.requireNonNull(response.getBody()).toString());

                // 결과를 저장할 리스트
                List<String> responseGuideList = new ArrayList<>();

                // 응답 내용 적절히 추출
                JsonNode messageNode = responseBody.path("choices").get(0).path("message").path("content");

                //1개만 재생성할 경우
                if(requestDto.getNum()==1) {
                    String text =  messageNode.asText();

                    int periodIndex = text.indexOf('.');
                    int questionIndex = text.indexOf('?');

                    // periodIndex와 questionIndex 중 더 작은 값을 찾습니다.
                    int minIndex = -1;
                    if (periodIndex != -1 && questionIndex != -1) {
                        minIndex = Math.min(periodIndex, questionIndex);
                    } else if (periodIndex != -1) {
                        minIndex = periodIndex;
                    } else if (questionIndex != -1) {
                        minIndex = questionIndex;
                    }

                    // 마침표가 존재하는 경우 문자열을 자릅니다.
                    if (minIndex != -1) {
                        text = messageNode.asText().substring(0, minIndex + 1); // 마침표까지 포함하여 자릅니다.
                    }
                    text = text.replaceAll("^\\d+\\.\\s*", "").trim();
                    text = text.replaceAll("\\\"", "").trim();
                    responseGuideList.add(text);
                    return responseGuideList.toArray(new String[0]);
                }

                log.info(messageNode.toString());
                // 문자열을 줄 단위로 분리
                List<String> lines = Arrays.asList(messageNode.asText().split("\n"));



                // 각 줄을 순회하며 문장만 추출
                for (String line : lines) {
                    // 각 줄이 하이픈으로 시작하는지 확인하고, 하이픈과 공백을 제거
                    String cleanedLine = line.replaceAll("^\\d+\\.\\s*", "").trim();
                    if(cleanedLine.length()<1) {
                        continue;
                    }
                    responseGuideList.add(cleanedLine);
                }
                return responseGuideList.toArray(new String[0]);
            }
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        }
    }
}
