package ssafy.mozip.group.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.presentation.GroupController;
import com.ssafy.mozip.group.dto.request.GroupCreateRequest;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.oauth2.domain.AuthTokens;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
@AutoConfigureRestDocs
public class GroupControllerTest {

    static final AuthTokens USER_TOKENS = new AuthTokens("admin", "admin");
    static final Cookie COOKIE = new Cookie("refresh-token", USER_TOKENS.refreshToken());

    @MockBean
    GroupService groupService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("신규 모임을 생성할 수 있다.")
    public void createGroup() throws Exception {
        // given
        String socialId = "testSocialId";
        String name = "Test User";
        String profileImage = "http://image.url";
        String email = "testuser@example.com";

        Member member = Member.of(socialId, name, profileImage, email);
        
        GroupCreateRequest groupCreateRequest = new GroupCreateRequest("Test Group", Arrays.asList("test1@example.com", "test2@example.com"));

        doNothing().when(groupService).createGroup(groupCreateRequest, member);

        // when & then
        mockMvc.perform(post("/groups")
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupCreateRequest)))
                .andExpect(status().isOk())
                .andDo(document("create-group"));

        verify(groupService).createGroup(groupCreateRequest, member);
    }
}
