package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsKnowledge;

/**
 * 运维知识库Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsKnowledgeService 
{
    /**
     * 查询运维知识库
     * 
     * @param id 运维知识库主键
     * @return 运维知识库
     */
    public OpsKnowledge selectOpsKnowledgeById(Long id);

    /**
     * 查询运维知识库列表
     * 
     * @param opsKnowledge 运维知识库
     * @return 运维知识库集合
     */
    public List<OpsKnowledge> selectOpsKnowledgeList(OpsKnowledge opsKnowledge);

    /**
     * 新增运维知识库
     * 
     * @param opsKnowledge 运维知识库
     * @return 结果
     */
    public int insertOpsKnowledge(OpsKnowledge opsKnowledge);

    /**
     * 修改运维知识库
     * 
     * @param opsKnowledge 运维知识库
     * @return 结果
     */
    public int updateOpsKnowledge(OpsKnowledge opsKnowledge);

    /**
     * 批量删除运维知识库
     * 
     * @param ids 需要删除的运维知识库主键集合
     * @return 结果
     */
    public int deleteOpsKnowledgeByIds(Long[] ids);

    /**
     * 删除运维知识库信息
     * 
     * @param id 运维知识库主键
     * @return 结果
     */
    public int deleteOpsKnowledgeById(Long id);
    public int incrementViewCount(Long id);
}
