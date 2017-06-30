package com.asgab.service.account;


import com.asgab.entity.DataSharing;
import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.service.report.ReportService;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShiroDbRealm extends AuthorizingRealm {

  protected AccountService accountService;

  // public static final List<String> adminGroupIds = Lists.newArrayList("414");
  private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

  /**
   * 认证回调函数,登录时调用.
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    User user = accountService.findUserByEmail(token.getUsername());

    if (user == null) {
      throw new UnknownAccountException();// 没找到帐号
    }
    // user.setStatus("Paused");
    /*
     * if( "Paused".equals(user.getStatus())) { throw new LockedAccountException(); //帐号锁定 }
     */

    token.setPassword((String.valueOf(token.getPassword()) + "wibble" + user.getSalt()).toCharArray());

    /* byte[] salt = Encodes.decodeHex(user.getSalt()); */

    // 如果是ceo，不需要设置数据共享
    if (user.getId().equals(ReportService.ceoUserId)) {
      user.setHasSetDataShare(true);
    }

    DataSharing dataSharing = accountService.getAssistant(user.getId());
    Long master_id = null;
    String master_bu = null;
    Boolean hasSetDataShare = user.getHasSetDataShare();
    if (dataSharing != null) {
      User masterUser = accountService.get(dataSharing.getUser_id());
      master_id = masterUser.getId();
      master_bu = masterUser.getBu();
      user.setHasSetDataShare(masterUser.getHasSetDataShare());
    }

    Long currency_id = null;
    DataSharing myDataSharing = accountService.getByUserId(user.getId());
    if (myDataSharing != null) {
      currency_id = myDataSharing.getCurrency_id();
    }

    ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName(), user.getBu(), hasSetDataShare, master_id, master_bu, currency_id);

    return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), getName());

    /*
     * return new SimpleAuthenticationInfo( new ShiroUser(user.getId(), user.getLoginName(),
     * user.getName()), user.getPassword(), ByteSource.Util.bytes(salt), getName());
     */

  }

  /**
   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    Long id = null;
    String bu = "";
    // 如果是助理.
    ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
    
   // 修改最后登录时间
    try {
      int result = accountService.update_logged(shiroUser.id);
      if (result == 0) {
        logger.warn("update_logged execute fail. userid is {}  ", shiroUser.id);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("update_logged execute has errors. userid is {}  ", shiroUser.id);
    }
    
    if (shiroUser.master_id != null) {
      id = shiroUser.master_id;
      bu = shiroUser.master_bu;
    } else {
      id = shiroUser.id;
      bu = shiroUser.getBu();
    }

    List<Group> groups = accountService.findUserGroupByUserId(id);

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    List<Long> canSeeGpIds = null;
    // BU是国外的都能看预估毛利 海外：intl， 其他都是国内
    if (StringUtils.isNotBlank(bu) && !bu.toLowerCase().contains("Intl".toLowerCase())) {
      // booking中配置的能否查看预估GP的userId
      canSeeGpIds = accountService.findCanSeeGp();

    } else if (StringUtils.isNotBlank(bu) && bu.toLowerCase().contains("Intl".toLowerCase())) {
      info.addStringPermission("gp:query");
    } else {
      canSeeGpIds = accountService.findCanSeeGp();
    }

    if (canSeeGpIds != null && canSeeGpIds.contains(id)) {
      info.addStringPermission("gp:query");
    }

    // if (SecurityUtils.getSubject().isPermitted("gp:query")) {
    //
    // }


    List<String> roles = new ArrayList<>();
    if (groups != null) {
      for (Group group : groups) {
        if (group == null)
          continue;
        if ("Super Administrators".equals(group.getGroupType())) {
          roles.add("superAdmin");
          info.addStringPermission("gp:query");
          info.addStringPermission("agency");
        }
        if ("Administrators".equals(group.getGroupType())) {
          roles.add("admin");
          info.addStringPermission("gp:query");
          info.addStringPermission("agency");
        }
        if ("Product Marketing".equalsIgnoreCase(group.getName())) {
          roles.add("product");
          info.addStringPermission("gp:query");
        }
        if ("Corporate Finance".equalsIgnoreCase(group.getName())) {
          roles.add("corporate");
          info.addStringPermission("gp:query");
        }
      }
    }
    info.addRoles(roles);
    List<Long> userIds = accountService.canSeeChannel(shiroUser.id);
    if (userIds!=null && userIds.size()>0){
      info.addStringPermission("agency");
    }
    return info;
  }

  public boolean containsAny(List<Group> groups, List<String> groupsIds) {

    if (groupsIds != null && groups != null) {
      for (String id : groupsIds) {
        if (groups.contains(new Group(id))) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * 设定Password校验的Hash算法与迭代次数.
   */
  @PostConstruct
  public void initCredentialsMatcher() {
    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
    // matcher.setHashIterations(AccountService.HASH_INTERATIONS);
    setCredentialsMatcher(matcher);
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
   */
  public static class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public Long id;
    public String loginName;
    public String name;
    public String bu;
    public Boolean hasSetDataShare;
    // 代理的id
    public Long master_id;
    public String master_bu;
    public Long currency_id;

    public ShiroUser(Long id, String loginName, String name, String bu, Boolean hasSetDataShare, Long master_id, String master_bu, Long currency_id) {
      this.id = id;
      this.loginName = loginName;
      this.name = name;
      this.bu = bu;
      this.hasSetDataShare = hasSetDataShare;
      this.master_id = master_id;
      this.master_bu = master_bu;
      this.currency_id = currency_id;
    }

    public String getName() {
      return name;
    }

    public String getBu() {
      return bu;
    }

    public Long getId() {
      return id;
    }

    public Boolean getHasSetDataShare() {
      return hasSetDataShare;
    }

    public Long getMaster_id() {
      return master_id;
    }

    public String getMaster_bu() {
      return master_bu;
    }

    public Long getCurrency_id() {
      return currency_id;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
      return loginName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
      return Objects.hashCode(loginName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ShiroUser other = (ShiroUser) obj;
      if (loginName == null) {
        if (other.loginName != null) {
          return false;
        }
      } else if (!loginName.equals(other.loginName)) {
        return false;
      }
      return true;
    }
  }
}
