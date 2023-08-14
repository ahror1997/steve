package de.rwth.idsg.steve.web.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskIDResp {
    private final Integer taskID;
}