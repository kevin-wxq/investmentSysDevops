package com.ruoyi.devops.service;
import java.util.List;
import com.ruoyi.devops.domain.OpsVendorContact;
public interface IOpsVendorContactService {
    public List<OpsVendorContact> selectOpsVendorContactList(OpsVendorContact vendorContact);
    public OpsVendorContact selectOpsVendorContactById(Long id);
    public int insertOpsVendorContact(OpsVendorContact vendorContact);
    public int updateOpsVendorContact(OpsVendorContact vendorContact);
    public int deleteOpsVendorContactByIds(Long[] ids);
}
