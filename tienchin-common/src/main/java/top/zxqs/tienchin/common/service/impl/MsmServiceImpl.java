package top.zxqs.tienchin.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.QuerySendStatisticsRequest;
import com.aliyun.dysmsapi20170525.models.QuerySendStatisticsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.common.core.domain.AjaxResult;
import top.zxqs.tienchin.common.service.MsmService;
import top.zxqs.tienchin.common.utils.ServletUtils;
import top.zxqs.tienchin.common.utils.StringUtils;

import java.util.Map;

/**
 * @author: zxq
 * @create: 2023-02-24 23:04
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Value("${tienchin.aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${tienchin.aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 发送验证码
     * @param param     验证码
     * @param phone     手机号
     * @return
     */
    @Override
    public void send(Map<String, Object> param, String phone) throws Exception {
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        Client client = MsmServiceImpl.createClient(accessKeyId, accessKeySecret);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam(JSONObject.toJSONString(param));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            ServletUtils.renderString(ServletUtils.getResponse(), JSON.toJSONString(AjaxResult.error(200, "发送成功")));
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}
