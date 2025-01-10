package ssafy.mozip.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.dto.request.GroupCreateRequest;
import com.ssafy.mozip.group.presentation.GroupController;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ssafy.mozip.common.controller.ControllerTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(GroupController.class)
class GroupControllerTest extends ControllerTest {

    @MockitoBean
    GroupService groupService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("모임을 생성할 수 있다.")
    public void testCreateGroup() throws Exception {

        Long groupId = 1L;
        var request = new GroupCreateRequest("Test Group", List.of("test1@email.com", "test2@email.com"));

        given(groupService.createGroup(any(), any()))
                .willReturn(groupId);

        mockMvc.perform(post("/groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header(HttpHeaders.AUTHORIZATION, "access-token")
                    .cookie(new Cookie("refresh-token", "refresh-token")))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(groupService).createGroup(any(), any());
    }
}
