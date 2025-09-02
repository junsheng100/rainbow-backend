package com.rainbow.user.service.impl;

import com.rainbow.base.client.UserClient;
import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.enums.UserType;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.router.MetaVo;
import com.rainbow.base.model.router.RouterVo;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.entity.SysRoleMenu;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserRole;
import com.rainbow.user.enums.MenuTypeEnum;
import com.rainbow.user.resource.SysMenuDao;
import com.rainbow.user.resource.SysRoleMenuDao;
import com.rainbow.user.resource.UserInfoDao;
import com.rainbow.user.resource.UserRoleDao;
import com.rainbow.user.service.RouterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RouterServiceImpl implements RouterService {
  @Autowired
  protected UserClient userClient;
  @Autowired
  private SysMenuDao menuDao;
  @Autowired
  private UserInfoDao userDao;
  @Autowired
  private UserRoleDao userRoleDao;
  @Autowired
  private SysRoleMenuDao roleMenuDao;

  private final static String NO_FRAME = UseStatus.YES.getCode();
  private final static String TYPE_DIR = MenuTypeEnum.CATALOG.getCode();
  private final static String TYPE_MENU = MenuTypeEnum.LEAF.getCode();
  private final static String YES = UseStatus.YES.getCode();
  private final static String NO = UseStatus.NO.getCode();
  private final static Long MENU_ROOT = DataConstant.MENU_ROOT;
  private final static String INNER_LINK = DataConstant.INNER_LINK;


  @Override
  public List<RouterVo> getRouters() {
    List<RouterVo> list = new ArrayList<>();

    LoginUser user = userClient.getLoginUser();
    String userId = user.getUserId();
    UserInfo userInfo = userDao.get(userId);
    String userType = userInfo.getUserType();
    boolean isAdmin = UserType.ADMIN.name().equals(userType);

    List<SysMenu> menuList = isAdmin ? menuDao.findMenuAll() : findMenuListByUserId(userId);
    List<RouterVo> dataList = getRouterPerms(menuList);


    dataList = dataList.stream().filter(t -> MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());

    return dataList;
  }

  private void getRouterListPerms(RouterVo vo, List<RouterVo> list,Integer index) {
    if (null != vo) {
      vo.setOrderLevel(index);
      list.add(vo);
      index++;
      if (CollectionUtils.isNotEmpty(vo.getChildren())) {
        for (RouterVo next : vo.getChildren()) {
          getRouterListPerms(next, list,index);
        }
      }
    }
  }

  private List<RouterVo> getRouterPerms(List<SysMenu> menuList) {
    List<RouterVo> list = new ArrayList<>();

    for (SysMenu menu : menuList) {
      RouterVo vo = convertSysMenu(menu);
      list.add(vo);
    }

    for (RouterVo vo : list) {
      List<RouterVo> children = list.stream().filter(t -> t.getParentId().equals(vo.getId())).collect(Collectors.toList());
      vo.setChildren(children);
    }

    return list;
  }


  private List<SysMenu> getChildPerms(List<SysMenu> list) {
    List<SysMenu> returnList = new ArrayList<SysMenu>();
    list.stream().forEach(menu -> {
      List<SysMenu> children = list.stream().filter(t -> menu.getMenuId().equals(t.getParentId())).collect(Collectors.toList());
      menu.setChildren(children);
    });
    returnList = list.stream().filter(t -> MENU_ROOT.equals(t.getPerms())).collect(Collectors.toList());
    return returnList;
  }

  @SneakyThrows
  @Override
  public List<SysMenu> findMenuListByUserId(String userId) {
    List<SysMenu> list = new ArrayList<>();
    List<UserRole> userRoleList = userRoleDao.findByUserId(userId);
    if (CollectionUtils.isEmpty(userRoleList))
      throw new AuthException("未授权");
    List<Long> roleIdList = userRoleList.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());

    List<SysRoleMenu> roleMenuList = roleMenuDao.findInRoleIdList(roleIdList);
    if (CollectionUtils.isEmpty(roleMenuList))
      throw new AuthException("无可用权限");

    List<Long> menuIdList = roleMenuList.stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
    list = menuDao.findMenuInMenuId(menuIdList);

    return list;
  }

  private RouterVo convertSysMenu(SysMenu menu) {
    try {
      RouterVo router = new RouterVo();
      router.setId(menu.getMenuId());
      router.setParentId(menu.getParentId());
      router.setOrderNum(menu.getOrderNum());

      boolean isHidden = isHidden(menu);
      router.setHidden(isHidden);
      router.setName(getRouteName(menu));
      router.setPath(getRouterPath(menu));
      router.setComponent(getComponent(menu));
      router.setQuery(menu.getQuery());
      boolean isCache = isCache(menu);
      router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), isCache, menu.getPath()));

      if (TYPE_DIR.equals(menu.getMenuType())) {
        router.setAlwaysShow(true);
//        router.setRedirect("noRedirect");
        router.setRedirect("");
      } else if (isMenuFrame(menu)) {
        router.setMeta(null);
        router.setPath(menu.getPath());
        router.setComponent(menu.getComponent());
        router.setName(getRouteName(menu.getRouteName(), menu.getPath()));
        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
        router.setQuery(menu.getQuery());
      } else if (MENU_ROOT.equals(menu.getParentId()) && isInnerLink(menu)) {
        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
        router.setPath("/");
        String routerPath = innerLinkReplaceEach(menu.getPath());
        router.setPath(routerPath);
        router.setComponent(INNER_LINK);
        router.setName(getRouteName(menu.getRouteName(), routerPath));
        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
      }
      return router;
    } catch (Exception e) {
      e.getMessage();
      log.error(e.getMessage());
      throw new BizException(e.getMessage());
    }
  }


  /**
   * 获取组件信息
   *
   * @param menu 菜单信息
   * @return 组件信息
   */
  public String getComponent(SysMenu menu) {
    String component = DataConstant.LAYOUT;
    if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
      component = menu.getComponent();
    } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
      component = DataConstant.INNER_LINK;
    } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
      component = DataConstant.PARENT_VIEW;
    }
    return component;
  }

  private String getRouterPath(SysMenu menu) {

    String routerPath = menu.getPath();
    // 内链打开外网方式
    if (!MENU_ROOT.equals(menu.getParentId())) {
      if (isInnerLink(menu)) {
        routerPath = innerLinkReplaceEach(routerPath);
      }
    }
    // 非外链并且是一级目录（类型为目录）
    if (MENU_ROOT.equals(menu.getParentId())) {
      if (TYPE_DIR.equals(menu.getMenuType())) {
        if (NO_FRAME.equals(menu.getMenuType())) {
          routerPath = "/" + menu.getPath();
        }
      }
    } else if (isMenuFrame(menu)) {
      routerPath = "/";
    }
    return routerPath;

  }


  /**
   * 是否为parent_view组件
   *
   * @param menu 菜单信息
   * @return 结果
   */
  public boolean isParentView(SysMenu menu) {
    return menu.getParentId().intValue() != 0 && TYPE_DIR.equals(menu.getMenuType());
  }


  /**
   * 判断是否有子节点
   */
  private boolean hasChild(List<SysMenu> list, SysMenu menu) {
    Long count = list.stream().filter(t -> menu.getMenuId().equals(t.getParentId())).count();
    return count > 0L;
  }


  public String getRouteName(SysMenu menu) {
    // 非外链并且是一级目录（类型为目录）
    if (isMenuFrame(menu)) {
      return StringUtils.EMPTY;
    }
    return getRouteName(menu.getRouteName(), menu.getPath());
  }


  public String getRouteName(String name, String path) {
    String routerName = StringUtils.isNotEmpty(name) ? name : path;
    return StringUtils.capitalize(routerName);
  }


  /**
   * 是否为菜单内部跳转
   *
   * @param menu 菜单信息
   * @return 结果
   */
  public boolean isMenuFrame(SysMenu menu) {
    if (MENU_ROOT.equals(menu.getParentId())) {
      if (TYPE_MENU.equals(menu.getMenuType())) {
        if (NO_FRAME.equals(menu.getIsFrame())) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * 是否为内链组件
   *
   * @param menu 菜单信息
   * @return 结果
   */
  public boolean isInnerLink(SysMenu menu) {
    return menu.getIsFrame().equals(NO_FRAME) && StringUtils.ishttp(menu.getPath());
  }


  /**
   * 内链域名特殊字符替换
   *
   * @return 替换后的内链域名
   */
  public String innerLinkReplaceEach(String path) {
    return StringUtils.replaceEach(path, new String[]{DataConstant.HTTP, DataConstant.HTTPS, DataConstant.WWW, ".", ":"},
            new String[]{"", "", "", "/", "/"});
  }

  private boolean isCache(SysMenu menu) {
    if (null != menu) {
      if (StringUtils.isNotBlank(menu.getIsCache())) {
        return YES.equals(menu.getIsCache());
      }
    }
    return false;
  }

  private boolean isHidden(SysMenu menu) {
    if (null != menu) {
      if (StringUtils.isNotBlank(menu.getVisible())) {
        return YES.equals(menu.getVisible());
      }
    }
    return false;
  }

}
