package com.ruoyi.devops.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.devops.domain.HtBugRecord;

/**
 * Bug记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface HtBugRecordMapper
{
    public HtBugRecord selectHtBugRecordById(Long id);

    public List<HtBugRecord> selectHtBugRecordList(HtBugRecord htBugRecord);

    public String selectMaxBugNoByPrefix(@Param("prefix") String prefix);

    public int insertHtBugRecord(HtBugRecord htBugRecord);

    public int updateHtBugRecord(HtBugRecord htBugRecord);

    public int deleteHtBugRecordById(Long id);

    public int deleteHtBugRecordByIds(Long[] ids);
}
