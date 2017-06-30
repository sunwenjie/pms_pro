package com.asgab.service.business.opportunity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.asgab.core.pagination.Page;
import com.asgab.entity.BusinessOpportunity;
import com.asgab.entity.BusinessOpportunityProduct;
import com.asgab.entity.BusinessOpportunityRemark;
import com.asgab.entity.ProgressBar;
import com.asgab.entity.User;
import com.asgab.entity.xmo.Currency;
import com.asgab.repository.BusinessOpportunityMapper;
import com.asgab.repository.BusinessOpportunityProductMapper;
import com.asgab.repository.ProductCategoryMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.product.ProductService;
import com.asgab.util.CommonUtil;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class BusinessOpportunityService {

  @Autowired
  private BusinessOpportunityMapper businessOpportunityMapper;

  @Autowired
  private BusinessOpportunityProductMapper businessOpportunityProductMapper;

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductCategoryMapper productCategoryMapper;

  @Autowired
  private UserXMOMapper userXMOMapper;

  public static Map<Integer, Integer> statusMap = new TreeMap<Integer, Integer>();
  public static Map<Integer, String> statusZH = new TreeMap<Integer, String>();
  public static Map<Integer, String> statusEN = new TreeMap<Integer, String>();

  static {
    statusMap.put(0, 1);
    statusMap.put(10, 2);
    statusMap.put(30, 3);
    statusMap.put(50, 4);
    statusMap.put(70, 5);
    statusMap.put(90, 6);
    statusMap.put(100, 7);

    /*
     * statusZH.put(1, "丢失"); statusZH.put(2, "商机初步跟进中"); statusZH.put(3, "商机已转为订单");
     * statusZH.put(4, "订单已审批通过"); statusZH.put(5, "排期表已分享给客户"); statusZH.put(6, "客户已确认排期表");
     * statusZH.put(7, "客户已签约");
     * 
     * statusEN.put(1, "Lost"); statusEN.put(2, "Draft"); statusEN.put(3, "Converted to order");
     * statusEN.put(4, "Order approved"); statusEN.put(5, "Media plan shared to client");
     * statusEN.put(6, "Media plan confirmed"); statusEN.put(7, "Contract signed");
     */

    statusZH.put(1, CommonUtil.getProperty("zh", "CN", "business.opportunity.status0"));
    statusZH.put(2, CommonUtil.getProperty("zh", "CN", "business.opportunity.status10"));
    statusZH.put(3, CommonUtil.getProperty("zh", "CN", "business.opportunity.status30"));
    statusZH.put(4, CommonUtil.getProperty("zh", "CN", "business.opportunity.status50"));
    statusZH.put(5, CommonUtil.getProperty("zh", "CN", "business.opportunity.status70"));
    statusZH.put(6, CommonUtil.getProperty("zh", "CN", "business.opportunity.status90"));
    statusZH.put(7, CommonUtil.getProperty("zh", "CN", "business.opportunity.status100"));

    statusEN.put(1, CommonUtil.getProperty("en", "US", "business.opportunity.status0"));
    statusEN.put(2, CommonUtil.getProperty("en", "US", "business.opportunity.status10"));
    statusEN.put(3, CommonUtil.getProperty("en", "US", "business.opportunity.status30"));
    statusEN.put(4, CommonUtil.getProperty("en", "US", "business.opportunity.status50"));
    statusEN.put(5, CommonUtil.getProperty("en", "US", "business.opportunity.status70"));
    statusEN.put(6, CommonUtil.getProperty("en", "US", "business.opportunity.status90"));
    statusEN.put(7, CommonUtil.getProperty("en", "US", "business.opportunity.status100"));
  }

  public Page<BusinessOpportunity> search(Page<BusinessOpportunity> page) {
    List<BusinessOpportunity> businessOpportunitys = businessOpportunityMapper.search(page.getSearchMap(), page.getRowBounds());
    for (int i = 0; i < businessOpportunitys.size(); i++) {
      businessOpportunitys.get(i).setProgressBar(new ProgressBar(businessOpportunitys.get(i).getProgress()));
    }
    // 查询备注
    setRemarks(businessOpportunitys);
    int count = businessOpportunityMapper.count(page.getSearchMap());
    page.setContent(businessOpportunitys);
    page.setTotal(count);
    return page;
  }

  private void setRemarks(BusinessOpportunity businessOpportunity) {
    if (businessOpportunity != null) {
      List<BusinessOpportunity> businessOpportunities = new ArrayList<BusinessOpportunity>();
      businessOpportunities.add(businessOpportunity);
      setRemarks(businessOpportunities);
    }
  }

  private void setRemarks(List<BusinessOpportunity> businessOpportunities) {
    if (businessOpportunities != null && businessOpportunities.size() > 0) {
      List<Long> ids = new ArrayList<Long>();
      for (BusinessOpportunity businessOpportunity : businessOpportunities) {
        ids.add(businessOpportunity.getId());
      }
      List<BusinessOpportunityRemark> remarks = businessOpportunityMapper.getRemarksByOpportunityIds(ids);
      for (BusinessOpportunity businessOpportunity : businessOpportunities) {
        for (BusinessOpportunityRemark remark : remarks) {
          if (businessOpportunity.getId().equals(remark.getBusiness_opportunity_id())) {
            businessOpportunity.getBusinessOpportunityRemarks().add(remark);
          }
        }
      }
    }
  }

  // 查询全部. 下载用
  public List<BusinessOpportunity> searchAll(Page<BusinessOpportunity> page) {
    return businessOpportunityMapper.search(page.getSearchMap());
  }

  public BusinessOpportunity getModelAttribute(long id) {
    return businessOpportunityMapper.get(id);
  }

  public BusinessOpportunity get(Long id) {
    BusinessOpportunity businessOpportunity = businessOpportunityMapper.get(id);
    if (businessOpportunity.getCooperate_sales() != null && !"".equals(businessOpportunity.getCooperate_sales())) {

      String cooperate_sale_names = "";
      for (String salesId : businessOpportunity.getCooperate_sales().split(",")) {
        User user = userXMOMapper.get(Long.valueOf(salesId));
        if (user != null) {
          if ("".equalsIgnoreCase(cooperate_sale_names)) {
            cooperate_sale_names = user.getName();
          } else {
            cooperate_sale_names += ", " + user.getName();
          }
        }
      }

      businessOpportunity.setCooperate_sale_names(cooperate_sale_names);
    }
    businessOpportunity.setCurrencys(this.getCurrencys());
    // 设置own_sale
    User owner_sales_user = userXMOMapper.get(businessOpportunity.getOwner_sale());
    businessOpportunity.setOwner_sale_name(owner_sales_user == null ? "" : owner_sales_user.getName());
    businessOpportunity.setProgressBar(new ProgressBar(businessOpportunity.getProgress()));
    if (businessOpportunity != null) {
      businessOpportunity.setBusinessOpportunityProducts(businessOpportunityProductMapper.getByBusinessOpportunityId(id));
      for (int i = 0; i < businessOpportunity.getBusinessOpportunityProducts().size(); i++) {
        BusinessOpportunityProduct businessOpportunityProduct = businessOpportunity.getBusinessOpportunityProducts().get(i);
        businessOpportunityProduct.setProduct(productService.get(businessOpportunityProduct.getProduct_id()));
        businessOpportunityProduct.setProductCategory(productCategoryMapper.get(businessOpportunityProduct.getProduct_category_id()));
      }
    }

    // 设置备注
    setRemarks(businessOpportunity);
    return businessOpportunity;
  }

  public void save(BusinessOpportunity businessOpportunity) {
    businessOpportunity.setStatus(statusMap.get(businessOpportunity.getProgress()));
    businessOpportunity.setCreated_at(new Date());
    businessOpportunityMapper.save(businessOpportunity);
    // 保存备注进度
    BusinessOpportunityRemark businessOpportunityRemark = new BusinessOpportunityRemark();
    businessOpportunityRemark.reset(businessOpportunity);
    saveBusinessOpportunityRemark(businessOpportunityRemark);

    for (BusinessOpportunityProduct thisBOP : businessOpportunity.getBusinessOpportunityProducts()) {
      if (thisBOP != null && thisBOP.getProduct_id() != null && thisBOP.getProduct_category_id() != null && thisBOP.getSale_mode() != null && thisBOP.getBudget_format() != null) {
        thisBOP.setBusiness_opportunity_id(businessOpportunity.getId());
        thisBOP.setCreated_at(new Date());
        thisBOP.setBudget(new BigDecimal(thisBOP.getBudget_format().replaceAll(",", "")));
        businessOpportunityProductMapper.save(thisBOP);
      }
    }
  }

  public void copy(BusinessOpportunity businessOpportunity) {
    businessOpportunity.setProgress(10);
    businessOpportunity.setStatus(statusMap.get(businessOpportunity.getProgress()));
    businessOpportunity.setCreated_at(new Date());
    businessOpportunityMapper.save(businessOpportunity);
    for (BusinessOpportunityProduct thisBOP : businessOpportunity.getBusinessOpportunityProducts()) {
      thisBOP.setBusiness_opportunity_id(businessOpportunity.getId());
      thisBOP.setCreated_at(new Date());
      businessOpportunityProductMapper.save(thisBOP);
    }
  }

  public void update(BusinessOpportunity businessOpportunity) {

    // 修改商机数据
    updateDB(businessOpportunity);

  }

  public void updateDB(BusinessOpportunity businessOpportunity) {
    businessOpportunity.setUpdated_at(new Date());
    businessOpportunityMapper.update(businessOpportunity);
    // 保存备注进度
    BusinessOpportunityRemark businessOpportunityRemark = new BusinessOpportunityRemark();
    businessOpportunityRemark.reset(businessOpportunity);
    saveBusinessOpportunityRemark(businessOpportunityRemark);

    // 先删除
    if (businessOpportunity.getDeleteProductIds() != null && businessOpportunity.getDeleteProductIds().length > 0) {
      for (String delete_pId : businessOpportunity.getDeleteProductIds()) {
        if (StringUtils.isNotBlank(delete_pId)) {
          businessOpportunityProductMapper.delete(Long.valueOf(delete_pId));
        }
      }
    }

    for (BusinessOpportunityProduct thisBOP : businessOpportunity.getBusinessOpportunityProducts()) {
      if (thisBOP != null && thisBOP.getProduct_id() != null && thisBOP.getProduct_category_id() != null && thisBOP.getSale_mode() != null && thisBOP.getBudget_format() != null) {
        if (thisBOP.getId() != null) {
          thisBOP.setUpdated_at(new Date());
          thisBOP.setBusiness_opportunity_id(businessOpportunity.getId());
          thisBOP.setBudget(new BigDecimal(thisBOP.getBudget_format().replaceAll(",", "")));
          businessOpportunityProductMapper.update(thisBOP);
        } else {
          thisBOP.setBusiness_opportunity_id(businessOpportunity.getId());
          thisBOP.setCreated_at(new Date());
          thisBOP.setBudget(new BigDecimal(thisBOP.getBudget_format().replaceAll(",", "")));
          businessOpportunityProductMapper.save(thisBOP);
        }
      }
    }
  }

  public List<Currency> getCurrencys() {
    return businessOpportunityMapper.getCurrencys();
  }

  public List<SelectMapper> getCurrencyMappers() {
    List<Currency> currencys = businessOpportunityMapper.getCurrencys();
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    for (int i = 0; i < currencys.size(); i++) {
      mappers.add(new SelectMapper(currencys.get(i).getId() + "", currencys.get(i).getName()));
    }
    return mappers;
  }

  public int delete(Long id) {
    List<BusinessOpportunityProduct> products = businessOpportunityProductMapper.getByBusinessOpportunityId(id);
    for (BusinessOpportunityProduct p : products) {
      p.setDeleted_at(new Date());
      businessOpportunityProductMapper.delete(p.getId());
    }
    BusinessOpportunity delOpportunity = new BusinessOpportunity();
    delOpportunity.setId(id);
    delOpportunity.setDeleted_at(new Date());
    return businessOpportunityMapper.delete(delOpportunity);
  }

  public String createOrder(Long businessOpportunityId) {
    OutputStreamWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      String url = "";
      if (CommonUtil.isProduct()) {
        url = "http://sales.optimix.asia/zh-cn/orders/create_order_by_business_opportunity";
      } else {
        url = "http://salesstg.optimix.asia/zh-cn/orders/create_order_by_business_opportunity";
      }
      URL realUrl = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Authorization", "Token token=c576f0136149a2e2d9127b3901019855");

      conn.connect();
      out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      // 发送请求参数
      out.write("business_opportunity_id=" + businessOpportunityId);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

  public XSSFWorkbook downloadReport(List<BusinessOpportunity> businessOpportunitys, HttpServletRequest request) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    String country = localeResolver.resolveLocale(request).getCountry();

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet reportSheet = workbook.createSheet();

    String[] headerColumns = {"opportunity.id", "business.opportunity.name", "business.opportunity.advertiser", "business.opportunity.agency", "business.opportunity.currency", "business.opportunity.budget", "business.opportunity.created.by",
        "business.opportunity.days", "business.opportunity.status", "opportunity.label"};
    for (int i = 0; i < headerColumns.length; i++) {
      reportSheet.setColumnWidth(i, 15 * 256);
    }

    // Arial字体
    XSSFFont arialFont = workbook.createFont();
    arialFont.setFontName("Calibri（主题正文）");

    // 头部样式
    XSSFCellStyle topCellStyle = workbook.createCellStyle();
    topCellStyle.setFont(arialFont);
    topCellStyle.getFont().setBold(true);

    // 正文样式
    XSSFCellStyle centerCellStyle = workbook.createCellStyle();
    centerCellStyle.setFont(arialFont);

    // 会计样式
    XSSFCellStyle currencyCellStyle = workbook.createCellStyle();
    currencyCellStyle.setFont(arialFont);
    XSSFDataFormat currencyFomat = workbook.createDataFormat();
    currencyCellStyle.setDataFormat(currencyFomat.getFormat("#,##0.00_);(#,##0.00)"));


    int rowIndex = 0;
    XSSFRow rowHeader = reportSheet.createRow(rowIndex);
    for (int i = 0; i < headerColumns.length; i++) {
      XSSFCell cell = rowHeader.createCell(i);
      cell.setCellStyle(topCellStyle);
      cell.setCellValue(CommonUtil.getProperty(lang, country, headerColumns[i]));
    }
    // body
    for (int i = 0; i < businessOpportunitys.size(); i++) {
      rowIndex++;
      int colunmIndex = 0;
      XSSFRow rowBody = reportSheet.createRow(rowIndex);
      // id
      XSSFCell id = rowBody.createCell(colunmIndex++);
      id.setCellStyle(centerCellStyle);
      id.setCellValue(businessOpportunitys.get(i).getNumber());

      // name
      XSSFCell name = rowBody.createCell(colunmIndex++);
      name.setCellStyle(centerCellStyle);
      name.setCellValue(businessOpportunitys.get(i).getName());

      // advertiser
      XSSFCell advertiser = rowBody.createCell(colunmIndex++);
      advertiser.setCellStyle(centerCellStyle);
      advertiser.setCellValue(businessOpportunitys.get(i).getAdvertiser());

      // agency
      XSSFCell agency = rowBody.createCell(colunmIndex++);
      agency.setCellStyle(centerCellStyle);
      agency.setCellValue(businessOpportunitys.get(i).getAgency());

      // currencyName
      XSSFCell currencyName = rowBody.createCell(colunmIndex++);
      currencyName.setCellStyle(centerCellStyle);
      currencyName.setCellValue(businessOpportunitys.get(i).getCurrencyName());

      // formatBudget
      XSSFCell formatBudget = rowBody.createCell(colunmIndex++);
      formatBudget.setCellStyle(currencyCellStyle);
      formatBudget.setCellValue(businessOpportunitys.get(i).getBudget().doubleValue());

      // username
      XSSFCell username = rowBody.createCell(colunmIndex++);
      username.setCellStyle(centerCellStyle);
      username.setCellValue(businessOpportunitys.get(i).getUsername());

      // days
      XSSFCell days = rowBody.createCell(colunmIndex++);
      days.setCellStyle(centerCellStyle);
      days.setCellValue(businessOpportunitys.get(i).getDays());

      // status
      XSSFCell status = rowBody.createCell(colunmIndex++);
      status.setCellStyle(centerCellStyle);
      if ("zh".equalsIgnoreCase(lang) && "zh".equalsIgnoreCase(country)) {
        status.setCellValue(businessOpportunitys.get(i).getDecodeStatus("zh"));
      } else {
        status.setCellValue(businessOpportunitys.get(i).getDecodeStatus("en"));
      }

      // progress
      XSSFCell progress = rowBody.createCell(colunmIndex++);
      progress.setCellStyle(centerCellStyle);
      progress.setCellValue(businessOpportunitys.get(i).getProgress());
    }
    return workbook;
  }

  public String getOrderCodeByOpportunityId(Long id) {
    return businessOpportunityMapper.getOrderCodeByOpportunityId(id);
  }

  public int saveBusinessOpportunityRemark(BusinessOpportunityRemark businessOpportunityRemark) {
    if (StringUtils.isBlank(businessOpportunityRemark.getContent())) {
      return 0;
    }
    return businessOpportunityMapper.saveBusinessOpportunityRemark(businessOpportunityRemark);
  }

  public List<BusinessOpportunityRemark> getRemarksByOpportunityId(Long id) {
    return businessOpportunityMapper.getRemarksByOpportunityId(id);
  }

  public int countOrdersByOpportunityId(Long id) {
    return businessOpportunityMapper.countOrdersByOpportunityId(id);
  }

}
