package top.zxqs.tienchin.system.mapper;

import top.zxqs.tienchin.system.domain.SendLog;

/**
 * @author MC
 */
public interface SendLogMapper {

    int insertSendLog(SendLog sendLog);

    void updateSendLogStatusByMsgId(String msgId);
}
