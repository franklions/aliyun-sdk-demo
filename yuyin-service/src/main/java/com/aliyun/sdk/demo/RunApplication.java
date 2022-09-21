package com.aliyun.sdk.demo;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author flsh
 * @version 1.0
 * @date 2021/3/22
 * @since Jdk 1.8
 */
@SpringBootApplication
public class RunApplication {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @param regionId
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dyvmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        Config config = new Config();
        // 您的AccessKey ID
        config.accessKeyId = accessKeyId;
        // 您的AccessKey Secret
        config.accessKeySecret = accessKeySecret;
        // 您的可用区ID
        config.regionId = regionId;
        return new com.aliyun.dyvmsapi20170525.Client(config);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RunApplication.class,args);
    }

    //语音文件通知
    public static void sendCallByVoice() throws Exception {
        com.aliyun.dyvmsapi20170525.Client client = createClient("accessKeyId", "accessKeySecret", "regionId");
        com.aliyun.dyvmsapi20170525.models.SingleCallByVoiceRequest request = com.aliyun.dyvmsapi20170525.models.SingleCallByVoiceRequest.build(TeaConverter.buildMap(
                // 被叫显号，若您使用的模板为公共号池号码外呼模板，则该字段值必须为空；
                // 若您使用的模板为专属号码外呼模板，则必须传入已购买的号码，仅支持一个号码，您可以在语音服务控制台上查看已购买的号码。
                new TeaPair("calledShowNumber", "18850xxxx"),
                // 被叫号码。仅支持中国内地号码。一次请求仅支持一个被叫号。
                new TeaPair("calledNumber", "15750xxxx"),
                // 语音文件的语音ID。
                new TeaPair("voiceCode", "e271f3f2-e155-4366-a9f4-0fe55765b3ec.wav")
        ));
        com.aliyun.dyvmsapi20170525.models.SingleCallByVoiceResponse response = client.singleCallByVoice(request);
        System.out.println(response.getBody());
    }

    //查询通话记录
    public static void searchCallRecord() throws Exception {
        com.aliyun.dyvmsapi20170525.Client client = createClient("accessKeyId", "accessKeySecret", "regionId");
        com.aliyun.dyvmsapi20170525.models.QueryCallDetailByCallIdRequest request = com.aliyun.dyvmsapi20170525.models.QueryCallDetailByCallIdRequest.build(TeaConverter.buildMap(
                new TeaPair("callId", "100625930001^10019107xx"),
                new TeaPair("prodId", 11000000300004L),
                new TeaPair("queryDate", 1577255564)
        ));
        com.aliyun.dyvmsapi20170525.models.QueryCallDetailByCallIdResponse response = client.queryCallDetailByCallId(request);
    }
}
