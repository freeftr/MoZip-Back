package com.ssafy.mozip.group.dto.request;

import java.util.List;

public record AddParticipantsRequest(
        Long groupId,
        List<String> emails
) {
}
