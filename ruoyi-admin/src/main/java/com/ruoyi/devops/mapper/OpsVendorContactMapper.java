package com.ruoyi.devops.mapper;
import java.util.List;
import com.ruoyi.devops.domain.OpsVendorContact;
public interface OpsVendorContactMapper {
    public List<OpsVendorContact> selectOpsVendorContactList(OpsVendorContact vendorContact);
    public OpsVendorContact selectOpsVendorContactById(Long id);
    public int insertOpsVendorContact(OpsVendorContact vendorContact);
    public int updateOpsVendorContact(OpsVendorContact vendorContact);
    public int deleteOpsVendorContactById(Long id);
    public int deleteOpsVendorContactByIds(Long[] ids);
}
