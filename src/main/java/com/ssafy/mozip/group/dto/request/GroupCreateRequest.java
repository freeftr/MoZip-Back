package com.ssafy.mozip.group.dto.request;

import java.util.List;

public record GroupCreateRequest(
        String name,
        List<String> emails
) {
}
