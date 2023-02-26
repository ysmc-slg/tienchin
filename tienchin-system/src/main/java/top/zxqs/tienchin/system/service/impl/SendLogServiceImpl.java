package top.zxqs.tienchin.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.system.domain.SendLog;
import top.zxqs.tienchin.system.mapper.SendLogMapper;
import top.zxqs.tienchin.system.service.ISendLogService;

/**
 * @author: zxq
 * @create: 2023-02-25 22:08
 */
@Service
public class SendLogServiceImpl implements ISendLogService {

    @Autowired
    private SendLogMapper sendLogMapper;
    @Override
    public int insertSendLog(SendLog sendLog) {
        return sendLogMapper.insertSendLog(sendLog);
    }

    @Override
    public void updateSendLogStatusByMsgId(String msgId) {
        sendLogMapper.updateSendLogStatusByMsgId(msgId);
    }
}
