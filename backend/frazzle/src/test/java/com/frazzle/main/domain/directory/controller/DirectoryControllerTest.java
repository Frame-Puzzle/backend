package com.frazzle.main.domain.directory.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.InviteMemberRequestDto;
import com.frazzle.main.domain.directory.dto.UpdateDirectoryNameRequestDto;
import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.global.auth.WithMockAuthUser;
import com.frazzle.main.global.config.SecurityConfig;
import com.frazzle.main.global.models.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(
        controllers = DirectoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
public class DirectoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DirectoryService directoryService;

    @Mock
    private UserPrincipal userPrincipal;

    private CreateDirectoryRequestDto createDirectoryRequestDto;
    private UpdateDirectoryNameRequestDto updateDirectoryNameRequestDto;
    private int directoryId;
    private String email;
    private User member;
    private List<UserByEmailResponseDto> userByEmailResponseDtos;
    private InviteMemberRequestDto inviteMemberRequestDto;

    @BeforeEach
    public void setup(){
        member = User.createUser("2", "이싸피", "ssafy@gmail.com", "google");
        createDirectoryRequestDto = new CreateDirectoryRequestDto("친구", "B208");
        updateDirectoryNameRequestDto = new UpdateDirectoryNameRequestDto("싸피공통");
        directoryId = 1;
        email = "s";
        userByEmailResponseDtos = new ArrayList<>();
        userByEmailResponseDtos.add(UserByEmailResponseDto.createFindUserByEmailResponseDto(member));
        inviteMemberRequestDto = new InviteMemberRequestDto(member.getUserId());
    }

    @Test
    @DisplayName("디렉토리 생성 성공 테스트")
    @WithMockAuthUser(email = "ssafy@gmail.com")
    public void 디렉토리_생성_성공_테스트() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(createDirectoryRequestDto);

        //when
        BDDMockito.doNothing().when(directoryService).createDirectory(userPrincipal, createDirectoryRequestDto);

        //then
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/directories")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("디렉토리 수정 성공 테스트")
    @WithMockAuthUser(email = "ssafy@ssafy.com")
    public void 디렉토리_수정_성공_테스트() throws Exception{
        //given
        String requestBody = objectMapper.writeValueAsString(updateDirectoryNameRequestDto);

        //when
        BDDMockito.doNothing().when(directoryService).updateDirectoryName(userPrincipal, updateDirectoryNameRequestDto, directoryId);

        //then
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/directories/{directoryId}", directoryId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("이메일로 유저 조회 성공 테스트")
    @WithMockAuthUser(email = "ssafy@ssafy.com")
    public void 이메일로_유저_조회_성공_테스트() throws Exception {
        BDDMockito.given(directoryService.findUserByEmail(userPrincipal, email, directoryId)).willReturn(userByEmailResponseDtos);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/directories/{directoryId}/users/find?email={email}", directoryId, email)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("멤버 초대 성공 테스트")
    @WithMockAuthUser(email = "ssafy@ssafy.com")
    public void 멤버_초대_성공_테스트() throws Exception {
        String requestBody = objectMapper.writeValueAsString(inviteMemberRequestDto);

        BDDMockito.doNothing().when(directoryService).inviteMember(userPrincipal, inviteMemberRequestDto, directoryId);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/directories/{directoryId}/user", directoryId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}