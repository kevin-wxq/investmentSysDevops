package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.HtBugRecord;

/**
 * Bug记录Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IHtBugRecordService
{
    public HtBugRecord selectHtBugRecordById(Long id);

    public List<HtBugRecord> selectHtBugRecordList(HtBugRecord htBugRecord);

    public String generateBugNo();

    public int insertHtBugRecord(HtBugRecord htBugRecord);

    public int updateHtBugRecord(HtBugRecord htBugRecord);

    public int deleteHtBugRecordByIds(Long[] ids);

    public int deleteHtBugRecordById(Long id);
}
