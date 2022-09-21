package com.aliyun.sdk.demo.domain;

import lombok.Data;

import java.util.Map;

/**
 * @author flsh
 * @version 1.0
 * @date 2021/4/6
 * @since Jdk 1.8
 */
@Data
public class CallTtsRequest {
    private String callNumber;
    private String ttsCode;
    private Map<String,Object> params;
}
