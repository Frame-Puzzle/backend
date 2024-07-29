package com.frazzle.main.domain.directory.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.UpdateDirectoryNameRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
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

    private User user;
    private Directory directory;
    private CreateDirectoryRequestDto createDirectoryRequestDto;
    private UpdateDirectoryNameRequestDto updateDirectoryNameRequestDto;
    private int directoryId;

    @BeforeEach
    public void setup(){
        user = User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao");
        createDirectoryRequestDto = new CreateDirectoryRequestDto("친구", "B208");
        updateDirectoryNameRequestDto = new UpdateDirectoryNameRequestDto("싸피공통");
        directoryId = 1;
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
}