package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsKnowledgeMapper;
import com.ruoyi.devops.domain.OpsKnowledge;
import com.ruoyi.devops.service.IOpsKnowledgeService;

/**
 * 运维知识库Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsKnowledgeServiceImpl implements IOpsKnowledgeService 
{
    @Autowired
    private OpsKnowledgeMapper opsKnowledgeMapper;

    /**
     * 查询运维知识库
     * 
     * @param id 运维知识库主键
     * @return 运维知识库
     */
    @Override
    public OpsKnowledge selectOpsKnowledgeById(Long id)
    {
        return opsKnowledgeMapper.selectOpsKnowledgeById(id);
    }

    /**
     * 查询运维知识库列表
     * 
     * @param opsKnowledge 运维知识库
     * @return 运维知识库
     */
    @Override
    public List<OpsKnowledge> selectOpsKnowledgeList(OpsKnowledge opsKnowledge)
    {
        return opsKnowledgeMapper.selectOpsKnowledgeList(opsKnowledge);
    }

    /**
     * 新增运维知识库
     * 
     * @param opsKnowledge 运维知识库
     * @return 结果
     */
    @Override
    public int insertOpsKnowledge(OpsKnowledge opsKnowledge)
    {
        opsKnowledge.setCreateTime(DateUtils.getNowDate());
        return opsKnowledgeMapper.insertOpsKnowledge(opsKnowledge);
    }

    /**
     * 修改运维知识库
     * 
     * @param opsKnowledge 运维知识库
     * @return 结果
     */
    @Override
    public int updateOpsKnowledge(OpsKnowledge opsKnowledge)
    {
        opsKnowledge.setUpdateTime(DateUtils.getNowDate());
        return opsKnowledgeMapper.updateOpsKnowledge(opsKnowledge);
    }

    /**
     * 批量删除运维知识库
     * 
     * @param ids 需要删除的运维知识库主键
     * @return 结果
     */
    @Override
    public int deleteOpsKnowledgeByIds(Long[] ids)
    {
        return opsKnowledgeMapper.deleteOpsKnowledgeByIds(ids);
    }

    /**
     * 删除运维知识库信息
     * 
     * @param id 运维知识库主键
     * @return 结果
     */
    @Override
    public int deleteOpsKnowledgeById(Long id)
    {
        return opsKnowledgeMapper.deleteOpsKnowledgeById(id);
    }

    @Override
    public int incrementViewCount(Long id)
    {
        return opsKnowledgeMapper.incrementViewCount(id);
    }
}
