package com.ssafy.mozip.group.dto;

import java.util.List;

public record AddParticipantsRequest(
        Long groupId,
        List<String> emails
) {
}
