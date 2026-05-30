package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsKnowledgeAttach;

public interface IOpsKnowledgeAttachService {
    public OpsKnowledgeAttach selectOpsKnowledgeAttachById(Long id);
    public List<OpsKnowledgeAttach> selectOpsKnowledgeAttachByKnowledgeId(Long knowledgeId);
    public int insertOpsKnowledgeAttach(OpsKnowledgeAttach attach);
    public int deleteOpsKnowledgeAttachById(Long id);
    public int deleteOpsKnowledgeAttachByKnowledgeId(Long knowledgeId);
}
