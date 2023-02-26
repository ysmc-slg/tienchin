package top.zxqs.tienchin.system.service;

import top.zxqs.tienchin.system.domain.SendLog;

public interface ISendLogService {
    /**
     * 添加 SendLog
     * @param sendLog
     * @return
     */
    int insertSendLog(SendLog sendLog);

    void updateSendLogStatusByMsgId(String msgId);
}
