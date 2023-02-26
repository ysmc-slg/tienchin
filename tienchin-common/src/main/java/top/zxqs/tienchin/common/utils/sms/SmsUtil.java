package top.zxqs.tienchin.common.utils.sms;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.stereotype.Component;
import top.zxqs.tienchin.common.config.TienChinConfig;
import top.zxqs.tienchin.common.exception.ServiceException;

import java.util.Map;

/**
 * @author: zxq
 * @create: 2023-02-25 13:36
 */
@Component
public class SmsUtil {
    /**
     * 发送验证码
     * @param map 手机号 和 验证码
     * @return
     */
    public static void send(Map<String,Object> map) throws Exception {

        String phoneNumber = ((String) map.get("phoneNumber"));
        String code = ((String) map.get("code"));

        String accessKeyId = TienChinConfig.getAliyunAccessKeyId();
        String accessKeySecret = TienChinConfig.getAliyunAccessKeySecret();
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        Client client = SmsUtil.createClient(accessKeyId,accessKeySecret);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("tienchin")
                .setTemplateCode("SMS_271360244")
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam(JSON.toJSONString(map));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
//            sendSmsResponse.getBody().getBizId()
        } catch (TeaException error) {
            // 如有需要，请打印 error
            System.out.println(error.getMessage());

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    private static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    /**
     * 验证手机号
     * @param phoneNumber
     */
    public static void checkPhoneNumber(String phoneNumber){

        //校验手机号（正则表达式）
        if (!phoneNumber.matches("1[3-9]\\d{9}")) {
            throw new ServiceException("手机号码不合规");
        }
    }

}
