package com.asgab.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asgab.util.CommonUtil;
import com.asgab.util.SelectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Client {

  /***
   * 广告主
   * 
   * @author Siuvan
   */

  // db
  private Long id;
  // 广告主名称/公司名称
  private String clientname;
  // 品牌
  private String brand;
  // 行业
  private int industry_id;
  // 是否是代理
  private int whether_channel;
  // 代理
  private String channel;
  // 货币
  private int currency_id;
  // 公司地址
  private String address;
  // 是否跨区
  private int whether_cross_district;
  // 资质名称/ 公司名字
  private String company_name;
  // 网站名称
  private String website_name;
  // 网址
  private String website_address;
  // 组织代码
  private String organization_code;
  // icp
  private String icp;
  // 营业执照
  private String business_licence;
  //
  private String organization_code_scan_file;
  private String business_licence_scan_file;
  private String icp_scan_file;

  private String status;
  private String state;

  // other
  private Date created_at;
  private String created_user;
  private Long user_id;
  private Date updated_at;

  // 广告主联系人
  private List<ClientContact> contacts = new ArrayList<>();

  // 广告主联系人的第一组数据存在这里
  private String contact_person;
  private String phone;
  private String email;
  private String position;

  // 删除的联系人ID
  private String[] deleteContactIds;

  private String dateRange;
  private String createDateStart;
  private String createDateEnd;

  private String buttonStyle;

  @JsonIgnore
  private List<SelectMapper> STATES_ZH = new ArrayList<SelectMapper>();
  @JsonIgnore
  private List<SelectMapper> STATES_EN = new ArrayList<SelectMapper>();

  // app
  private String channel_name;
  private String saleIds;
  private String saleNames;
  private String currencyTypeName;
  private String industryTypeNameZH;
  private String industryTypeNameEN;


  public Client() {
    super();
    STATES_ZH.add(new SelectMapper("", "全部"));
    STATES_ZH.add(new SelectMapper("cli_saved", "草稿"));
    STATES_ZH.add(new SelectMapper("unapproved", "审批中"));
    STATES_ZH.add(new SelectMapper("approved", "审批通过"));
    STATES_ZH.add(new SelectMapper("cli_rejected", "审批未通过"));
    STATES_ZH.add(new SelectMapper("cross_unapproved", "跨区特批中"));
    STATES_ZH.add(new SelectMapper("released", "已释放"));

    STATES_EN.add(new SelectMapper("", "All"));
    STATES_EN.add(new SelectMapper("cli_saved", "Draft"));
    STATES_EN.add(new SelectMapper("unapproved", "Approval in Progress"));
    STATES_EN.add(new SelectMapper("approved", "Approved"));
    STATES_EN.add(new SelectMapper("cli_rejected", "Rejected"));
    STATES_EN.add(new SelectMapper("cross_unapproved", "Transregional Approval in Progress"));
    STATES_EN.add(new SelectMapper("released", "Released"));
  }


  public List<SelectMapper> getSTATES_ZH() {
    return STATES_ZH;
  }

  public void setSTATES_ZH(List<SelectMapper> sTATES_ZH) {
    STATES_ZH = sTATES_ZH;
  }

  public List<SelectMapper> getSTATES_EN() {
    return STATES_EN;
  }

  public void setSTATES_EN(List<SelectMapper> sTATES_EN) {
    STATES_EN = sTATES_EN;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getIndustry_id() {
    return industry_id;
  }

  public void setIndustry_id(int industry_id) {
    this.industry_id = industry_id;
  }

  public int getWhether_channel() {
    return whether_channel;
  }

  public void setWhether_channel(int whether_channel) {
    this.whether_channel = whether_channel;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public int getCurrency_id() {
    return currency_id;
  }

  public void setCurrency_id(int currency_id) {
    this.currency_id = currency_id;
  }

  public int getWhether_cross_district() {
    return whether_cross_district;
  }

  public void setWhether_cross_district(int whether_cross_district) {
    this.whether_cross_district = whether_cross_district;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public String getCreated_user() {
    return created_user;
  }

  public void setCreated_user(String created_user) {
    this.created_user = created_user;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getChannel_name() {
    return channel_name;
  }

  public void setChannel_name(String channel_name) {
    this.channel_name = channel_name;
  }

  public List<ClientContact> getContacts() {
    return contacts;
  }

  public void setContacts(List<ClientContact> contacts) {
    this.contacts = contacts;
  }

  public String[] getDeleteContactIds() {
    return deleteContactIds;
  }

  public void setDeleteContactIds(String[] deleteContactIds) {
    this.deleteContactIds = deleteContactIds;
  }

  public String getSaleIds() {
    return saleIds;
  }

  public void setSaleIds(String saleIds) {
    this.saleIds = saleIds;
  }

  public String getSaleNames() {
    return saleNames;
  }

  public void setSaleNames(String saleNames) {
    this.saleNames = saleNames;
  }

  public String getWebsite_name() {
    return website_name;
  }

  public void setWebsite_name(String website_name) {
    this.website_name = website_name;
  }

  public String getWebsite_address() {
    return website_address;
  }

  public void setWebsite_address(String website_address) {
    this.website_address = website_address;
  }

  public String getOrganization_code() {
    return organization_code;
  }

  public void setOrganization_code(String organization_code) {
    this.organization_code = organization_code;
  }

  public String getIcp() {
    return icp;
  }

  public void setIcp(String icp) {
    this.icp = icp;
  }

  public String getBusiness_licence() {
    return business_licence;
  }

  public void setBusiness_licence(String business_licence) {
    this.business_licence = business_licence;
  }

  public String getOrganization_code_scan_file() {
    return organization_code_scan_file;
  }

  public void setOrganization_code_scan_file(String organization_code_scan_file) {
    this.organization_code_scan_file = organization_code_scan_file;
  }

  public String getBusiness_licence_scan_file() {
    return business_licence_scan_file;
  }

  public void setBusiness_licence_scan_file(String business_licence_scan_file) {
    this.business_licence_scan_file = business_licence_scan_file;
  }

  public String getIcp_scan_file() {
    return icp_scan_file;
  }

  public void setIcp_scan_file(String icp_scan_file) {
    this.icp_scan_file = icp_scan_file;
  }

  public String getDateRange() {
    return dateRange;
  }

  public void setDateRange(String dateRange) {
    this.dateRange = dateRange;
  }

  public String getCreateDateStart() {
    return createDateStart;
  }

  public void setCreateDateStart(String createDateStart) {
    this.createDateStart = createDateStart;
  }

  public String getCreateDateEnd() {
    return createDateEnd;
  }

  public void setCreateDateEnd(String createDateEnd) {
    this.createDateEnd = createDateEnd;
  }

  public String getClientname() {
    return clientname;
  }

  public void setClientname(String clientname) {
    this.clientname = clientname;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCompany_name() {
    return company_name;
  }

  public void setCompany_name(String company_name) {
    this.company_name = company_name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getButtonStyle() {
    buttonStyle = "default";

    if (this.state != null) {
      switch (state) {
        case "cli_saved":
          buttonStyle = "default";
          break;
        case "unapproved":
          buttonStyle = "primary";
          break;
        case "approved":
          buttonStyle = "success";
          break;
        case "cli_rejected":
          buttonStyle = "danger";
          break;
        case "cross_unapproved":
          buttonStyle = "primary";
          break;
        case "released":
          buttonStyle = "default";
          break;
        default:
          break;
      }
    }

    return buttonStyle;
  }

  public void setButtonStyle(String buttonStyle) {
    this.buttonStyle = buttonStyle;
  }

  public String getContact_person() {
    return contact_person;
  }

  public void setContact_person(String contact_person) {
    this.contact_person = contact_person;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getCurrencyTypeName() {
    return currencyTypeName;
  }

  public void setCurrencyTypeName(String currencyTypeName) {
    this.currencyTypeName = currencyTypeName;
  }

  public String getIndustryTypeNameZH() {
    return industryTypeNameZH;
  }

  public void setIndustryTypeNameZH(String industryTypeNameZH) {
    this.industryTypeNameZH = industryTypeNameZH;
  }

  public String getIndustryTypeNameEN() {
    return industryTypeNameEN;
  }

  public void setIndustryTypeNameEN(String industryTypeNameEN) {
    this.industryTypeNameEN = industryTypeNameEN;
  }

  public String getDecodeWhether_cross_district_zh() {
    if (0 == whether_cross_district)
      return CommonUtil.getProperty("zh", "CN", "client.channel.no");
    else if (1 == whether_cross_district)
      return CommonUtil.getProperty("zh", "CN", "client.channel.yes");
    return "";
  }

  public String getDecodeWhether_cross_district_en() {
    if (0 == whether_cross_district)
      return CommonUtil.getProperty("en", "US", "client.channel.no");
    else if (1 == whether_cross_district)
      return CommonUtil.getProperty("en", "US", "client.channel.yes");
    return "";
  }

  public String getNumber() {
    String ret = new String("AD");
    for (int fill = 0; fill < 9 - String.valueOf(id).length(); fill++) {
      ret += "0";
    }
    ret += id;
    return ret;
  }

}
