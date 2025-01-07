package com.ssafy.mozip.group.dto;

import java.util.List;

public record GroupCreateRequest(
        String name,
        List<String> emails
) {
}
