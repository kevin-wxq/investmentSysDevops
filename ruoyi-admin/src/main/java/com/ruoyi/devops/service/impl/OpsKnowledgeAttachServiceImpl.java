package com.ruoyi.devops.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.domain.OpsKnowledgeAttach;
import com.ruoyi.devops.mapper.OpsKnowledgeAttachMapper;
import com.ruoyi.devops.service.IOpsKnowledgeAttachService;

@Service
public class OpsKnowledgeAttachServiceImpl implements IOpsKnowledgeAttachService {
    @Autowired
    private OpsKnowledgeAttachMapper attachMapper;

    @Override
    public OpsKnowledgeAttach selectOpsKnowledgeAttachById(Long id) {
        return attachMapper.selectOpsKnowledgeAttachById(id);
    }

    @Override
    public List<OpsKnowledgeAttach> selectOpsKnowledgeAttachByKnowledgeId(Long knowledgeId) {
        return attachMapper.selectOpsKnowledgeAttachByKnowledgeId(knowledgeId);
    }

    @Override
    public int insertOpsKnowledgeAttach(OpsKnowledgeAttach attach) {
        return attachMapper.insertOpsKnowledgeAttach(attach);
    }

    @Override
    public int deleteOpsKnowledgeAttachById(Long id) {
        return attachMapper.deleteOpsKnowledgeAttachById(id);
    }

    @Override
    public int deleteOpsKnowledgeAttachByKnowledgeId(Long knowledgeId) {
        return attachMapper.deleteOpsKnowledgeAttachByKnowledgeId(knowledgeId);
    }
}
