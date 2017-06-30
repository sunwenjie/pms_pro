package com.asgab.web.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asgab.core.pagination.Page;
import com.asgab.entity.User;
import com.asgab.service.account.AccountService;
import com.asgab.util.Servlets;

/*
 * user管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /user/ Create page : GET /user/create Create action : POST /user/create Update
 * page : GET /user/update/{id} Update action : POST /user/update Delete action : GET
 * /user/delete/{id}
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

  private static final String PAGE_SIZE = "10";
  @Autowired
  AccountService accountService;

 

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("loginName"))) {
      params.put("loginName", request.getParameter("loginName"));
    }
    if (StringUtils.isNotBlank(request.getParameter("name"))) {
      params.put("name", request.getParameter("name"));
    }
    if (StringUtils.isNotBlank(request.getParameter("roles"))) {
      params.put("roles", request.getParameter("roles"));
    }

    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<User> page = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages", accountService.getAllUser(page));
    return "account/usersList";
  }

  // @RequestMapping(value = "create", method = RequestMethod.GET)
  // public String toCreate(Model model) {
  // model.addAttribute("user", new User());
  // model.addAttribute("action", "create");
  // return "account/userForm";
  // }

  // @RequestMapping(value = "create", method = RequestMethod.POST)
  // public String create(User user, RedirectAttributes redirectAttributes) {
  // accountService.registerUser(user);
  // redirectAttributes.addFlashAttribute("message", "create user success");
  // return "redirect:/user";
  // }

  // @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  // public String toUpdate(@PathVariable("id") Long id, Model model) {
  // model.addAttribute("user", accountService.getUser(id));
  // model.addAttribute("action", "update");
  // return "account/userForm";
  // }

  // @RequestMapping(value = "update", method = RequestMethod.POST)
  // public String update(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes)
  // {
  // accountService.updateUser(user);
  // redirectAttributes.addFlashAttribute("message", "update user success");
  // return "redirect:/user";
  // }

  // @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  // public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
  // accountService.deleteUser(id);
  // redirectAttributes.addFlashAttribute("message", "delete user success");
  // return "redirect:/user";
  // }

  /**
   * Ajax请求校验loginName是否唯一。
   */
  @RequestMapping(value = "checkLoginName")
  @ResponseBody
  public String checkLoginName(@RequestParam("loginName") String loginName) {
    if (accountService.findUserByEmail(loginName) == null) {
      return "true";
    } else {
      return "false";
    }
  }

 


}
