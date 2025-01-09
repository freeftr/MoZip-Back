package com.ssafy.mozip.group.dto.response;

import java.util.List;

public record GroupListResponse(
        List<GroupResponse> groups
) {
    public record GroupResponse(
            String groupName,
            String leaderName
    ) {
    }
}
