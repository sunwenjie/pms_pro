package com.asgab.web.report;

import com.asgab.entity.CurrencyType;
import com.asgab.entity.Report;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.channel.ChannelService;
import com.asgab.service.exchange.ExchangeRateService;
import com.asgab.service.management.CurrencyTypeService;
import com.asgab.service.product.ProductCategoryService;
import com.asgab.service.report.ReportService;
import com.asgab.service.setting.DataSharingService;
import com.asgab.util.CommonUtil;
import com.asgab.util.SelectMapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
@RequestMapping(value = "/report")
public class ReportController {

  @Autowired
  private CurrencyTypeService currencyTypeService;
  @Autowired
  private DataSharingService dataSharingService;
  @Autowired
  private ExchangeRateService exchangeRateService;
  @Autowired
  private ChannelService channelService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private ProductCategoryService productCategoryService;

  @Autowired
  private ReportService reportService;

  @RequestMapping(method = RequestMethod.GET)
  public String report(Model model, HttpServletRequest request) {

    List<SelectMapper> currencys = currencyTypeService.getOptions(null);
    currencys.add(0, new SelectMapper("0", CommonUtil.getProperty(request, "report.all")));
    model.addAttribute("currencys", currencys);
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    boolean isAdmin = SecurityUtils.getSubject().hasRole("admin") || SecurityUtils.getSubject().hasRole("superAdmin");
    // 销售代表
    List<SelectMapper> saleRepresentatives = null;
    if (isAdmin) {
      saleRepresentatives = accountService.getAllStatusUserMappers();
    } else {
      saleRepresentatives = dataSharingService.getUserMppersByUserId(user.id);
    }
    model.addAttribute("saleRepresentatives", saleRepresentatives);
    // 销售团队
    List<SelectMapper> saleTeams = null;
    if (isAdmin) {
      // saleTeams = dataSharingService.getAllSaleTeamMappers();
      saleTeams = dataSharingService.getTeamMpperByUserId(ReportService.ceoUserId);
    } else {
      saleTeams = dataSharingService.getTeamMpperByUserId(user.id);
    }
    model.addAttribute("saleTeams", saleTeams);
    // 货币
    model.addAttribute("exchangeRates", exchangeRateService.getCurrencyRMB2OthersJson());
    // channels
    model.addAttribute("channels", channelService.getChannelMapper());
    // 广告类别
    model.addAttribute("productCategories", productCategoryService.getListMapper());

    // 默认货币
    Long currency_id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).currency_id;
    CurrencyType currencyType = null;
    if (currency_id != null) {
      currencyType = currencyTypeService.getCurrencyTypeById(currency_id);
    }
    // 默认datasharing. 没有就RMB
    model.addAttribute("defaultCurrency", currencyType != null ? currencyType.getName() : "RMB");


    boolean gpHidden = true;
    if (SecurityUtils.getSubject().isPermitted("gp:query")) {
      gpHidden = false;
    }
    model.addAttribute("gpHidden", gpHidden);

    return "report/report";
  }

  /***
   * 导出报表
   * 
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "download")
  public void download(Report report, HttpServletRequest request, HttpServletResponse response) {

    String fileName = CommonUtil.getProperty(request, ReportService.dataRightColumns[Integer.parseInt(report.getDataRight()) - 1]) + "-";
    fileName += "2".equals(report.getMetric()) ? CommonUtil.getProperty(request, "report.estimate.gp") : CommonUtil.getProperty(request, "report.income");
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/vnd.ms-excel");
    try {
      response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(URLDecoder.decode(fileName + ".xlsx", "UTF-8").getBytes(), "iso-8859-1") + "\"");
    } catch (UnsupportedEncodingException uee) {
      uee.printStackTrace();
    }

    try {
      XSSFWorkbook workbook = reportService.downloadReport(report, request);
      workbook.write(response.getOutputStream());
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

  /**
   * dashboard
   * 
   * @param report
   * @param request
   * @param response
   */
  @RequestMapping(value = "downloadDashboard", method = RequestMethod.POST)
  public void downloadDashboard(Report report, HttpServletRequest request, HttpServletResponse response) {

    response.setCharacterEncoding("utf-8");
    response.setContentType("application/vnd.ms-excel");
    try {
      response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(URLDecoder.decode("Dashboard Data format.xlsx", "UTF-8").getBytes(), "iso-8859-1") + "\"");
    } catch (UnsupportedEncodingException uee) {
      uee.printStackTrace();
    }

    try {
      XSSFWorkbook workbook = reportService.downloadReportDashboard(report, request);
      workbook.write(response.getOutputStream());
      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

}
