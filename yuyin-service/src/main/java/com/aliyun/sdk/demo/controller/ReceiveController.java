package com.aliyun.sdk.demo.controller;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.dyvmsapi20170525.models.QueryCallDetailByCallIdRequest;
import com.aliyun.dyvmsapi20170525.models.QueryCallDetailByCallIdResponse;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.aliyun.sdk.demo.domain.CallTtsRequest;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author flsh
 * @version 1.0
 * @date 2021/3/23
 * @since Jdk 1.8
 */
@RestController
public class ReceiveController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody String body) {
        logger.info("Request Body={}",body);
        Map<String,Object> retval = new HashMap<>();
        retval.put("code",0);
        retval.put("msg","成功");
        return ResponseEntity.ok(retval);
    }

    /**
     * QPS限制
     * 单用户调用频率：1000次/秒。
     * API频率：4000次/秒。
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/calltts")
    public SingleCallByTtsResponse callPhone(@RequestBody CallTtsRequest req) throws Exception {
        Client client = createClient("", "");
        SingleCallByTtsRequest request = SingleCallByTtsRequest.build(TeaConverter.buildMap(
                // 被叫显号，若您使用的模板为公共号池号码外呼模板，则该字段值必须为空；
                // 若您使用的模板为专属号码外呼模板，则必须传入已购买的号码，仅支持一个号码，您可以在语音服务控制台上查看已购买的号码。
                new TeaPair("calledShowNumber",""),
                new TeaPair("calledNumber", req.getCallNumber()),
                // 被叫号码。仅支持中国内地号码。一次请求仅支持一个被叫号。
                new TeaPair("ttsCode", req.getTtsCode()),
                // 语音文件的语音ID。
                new TeaPair("ttsParam", objectMapper.writeValueAsString(req.getParams()))
        ));

        SingleCallByTtsResponse response = client.singleCallByTts(request);
        return response;
    }

    /**
     * 说明如果多个用户的总调用超过API频率也会触发流控。
     * QPS限制
     * 单用户调用频率：100次/秒。
     * API频率：500次/秒。
     * @param callid
     * @param prodId
     * @return
     * @throws Exception
     */
    @GetMapping("/query/calldetail")
    public QueryCallDetailByCallIdResponse queryCallData(@RequestParam("callid") String callid,
                                                         @RequestParam("prodid") Long prodId) throws Exception {
        Client client = createClient("", "");
        QueryCallDetailByCallIdRequest request = QueryCallDetailByCallIdRequest.build(TeaConverter.buildMap(

                new TeaPair("callId",callid),
                new TeaPair("prodId", prodId),
                new TeaPair("queryDate",System.currentTimeMillis())
        ));

        QueryCallDetailByCallIdResponse response = client.queryCallDetailByCallId(request);
        return response;
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public  com.aliyun.dyvmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        // 您的AccessKey ID
        config.accessKeyId = accessKeyId;
        // 您的AccessKey Secret
        config.accessKeySecret = accessKeySecret;
        // 您的可用区ID
        config.endpoint = "dyvmsapi.aliyuncs.com";
        return new com.aliyun.dyvmsapi20170525.Client(config);
    }
}
