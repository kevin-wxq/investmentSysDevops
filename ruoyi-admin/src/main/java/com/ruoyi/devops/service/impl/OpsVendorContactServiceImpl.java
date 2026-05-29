package com.ruoyi.devops.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.devops.domain.OpsVendorContact;
import com.ruoyi.devops.mapper.OpsVendorContactMapper;
import com.ruoyi.devops.service.IOpsVendorContactService;
@Service
public class OpsVendorContactServiceImpl implements IOpsVendorContactService {
    @Autowired
    private OpsVendorContactMapper vendorContactMapper;
    @Override public List<OpsVendorContact> selectOpsVendorContactList(OpsVendorContact vendorContact) { return vendorContactMapper.selectOpsVendorContactList(vendorContact); }
    @Override public OpsVendorContact selectOpsVendorContactById(Long id) { return vendorContactMapper.selectOpsVendorContactById(id); }
    @Override public int insertOpsVendorContact(OpsVendorContact vendorContact) {
        if (vendorContact.getOrderNum() == null) {
            vendorContact.setOrderNum(0);
        }
        vendorContact.setCreateTime(DateUtils.getNowDate());
        return vendorContactMapper.insertOpsVendorContact(vendorContact);
    }
    @Override public int updateOpsVendorContact(OpsVendorContact vendorContact) {
        vendorContact.setUpdateTime(DateUtils.getNowDate());
        return vendorContactMapper.updateOpsVendorContact(vendorContact);
    }
    @Override public int deleteOpsVendorContactByIds(Long[] ids) { return vendorContactMapper.deleteOpsVendorContactByIds(ids); }
}
