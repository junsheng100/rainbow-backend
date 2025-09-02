package com.rainbow.user.service.impl;

import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.entity.SysRoleMenu;
import com.rainbow.user.enums.MenuTypeEnum;
import com.rainbow.user.resource.SysMenuDao;
import com.rainbow.user.resource.SysRoleMenuDao;
import com.rainbow.user.resource.UserRoleDao;
import com.rainbow.user.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu, Long,SysMenuDao> implements SysMenuService {

  @Autowired
  private SysRoleMenuDao roleMenuDao;
  @Autowired
  private UserRoleDao userRoleDao;

  @Override
  public Boolean delete(Long id) {
    Assert.notNull(id, "ID 不能为空");
    SysMenu menu = baseDao.get(id);
    Assert.notNull(menu, "数据不存在");

    Long count = baseDao.countChildren(menu.getMenuId());
    if (count > 0L)
      throw new BizException("存在下级菜单，不能删除");

    List<SysRoleMenu> roleMenuList = roleMenuDao.findByMenuId(menu.getMenuId());
    if (CollectionUtils.isNotEmpty(roleMenuList)) {
      List<Long> roleIdList = roleMenuList.stream().map(SysRoleMenu::getRoleId).distinct().collect(Collectors.toList());
      userRoleDao.removeList(roleIdList);
      roleMenuDao.removeList(menu.getMenuId());
    }
    baseDao.remove(menu.getMenuId());

    return true;
  }


  @Override
  public List<SysMenu> findChildren(Long parentId) {

    List<SysMenu> list = baseDao.findChildren(parentId);
    convertCollection(list);
    return list;
  }

  @Override
  public List<SysMenu> findTreeList(String menuType) {
    Assert.notNull(menuType, "菜单类型不能为空");
    MenuTypeEnum type = MenuTypeEnum.getByCode(menuType);
    Assert.notNull(type, "菜单类型数据不能为空");

    String[] types = null;

    switch (type) {
      case CATALOG:
      case LEAF:
        types = new String[]{MenuTypeEnum.CATALOG.getCode()};
        break;
      case FUNCTION:
        types = new String[]{MenuTypeEnum.CATALOG.getCode(), MenuTypeEnum.LEAF.getCode()};
        break;
    }

    List<String> typeList = Arrays.asList(types);
    List<SysMenu> all = findInMenuType(typeList);


    if (CollectionUtils.isEmpty(all))
      return Collections.emptyList();
    all.stream().map(t -> {
      List<SysMenu> children = all.stream().filter(a -> t.getMenuId().equals(a.getParentId())).collect(Collectors.toList());
      t.setChildren(children);
      return t;
    }).collect(Collectors.toList());


    all.stream().forEach(m -> {
      List<SysMenu> children = all.stream().filter(a -> m.getMenuId().equals(a.getParentId())).collect(Collectors.toList());
      m.setChildren(children);
    });

    List<SysMenu> list = all.stream().filter(t -> DataConstant.MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());

    return list;
  }

  private List<SysMenu> findInMenuType(List<String> typeList) {
    List<SysMenu> list = baseDao.findInMenuType(typeList);
    convertCollection(list);
    return list;
  }

  @Override
  public List<SysMenu> findMenuTreeView(BaseVo<SysMenu> vo) {

    List<SysMenu> list = list(vo);

    if (CollectionUtils.isNotEmpty(list)) {
      List<SysMenu> all = baseDao.findAll();
      convertCollection(all);
      List<Long> idList = list.stream().map(SysMenu::getMenuId).distinct().collect(Collectors.toList());
      list = all.stream().filter(a -> idList.contains(a.getMenuId())).collect(Collectors.toList());
      List<SysMenu> parentLis = new ArrayList<>();
      getParentIdList(all, list, parentLis);
      if (CollectionUtils.isNotEmpty(parentLis)) {
        list.addAll(new ArrayList<>(new HashSet<>(parentLis)));
        list = new ArrayList<>(new HashSet<>(list));
      }
      for (SysMenu menu : list) {
        List<SysMenu> children = list.stream().filter(a -> a.getParentId().equals(menu.getMenuId())).collect(Collectors.toList());
        menu.setChildren(children);
      }
      list = list.stream().filter(t -> DataConstant.MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());
      return list;
    }
    return null;
  }

  @Override
  public List<SysMenu> findMenuTree() {
    List<SysMenu> all = findMenuAll();
    List<SysMenu> treeList = all.stream().filter(t -> DataConstant.MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());

    return treeList;
  }


  private List<SysMenu> findMenuAll() {
    List<SysMenu> all = baseDao.findAll();
    if (CollectionUtils.isNotEmpty(all)) {
      for (SysMenu menu : all) {
        List<SysMenu> children = all.stream().filter(a -> menu.getMenuId().equals(a.getParentId())).collect(Collectors.toList());
        menu.setChildren(children);
      }
    }

    return all;
  }

  private void getParentIdList(List<SysMenu> all, List<SysMenu> list, List<SysMenu> parentLis) {

    List<Long> pidList = CollectionUtils.isEmpty(list) ? null : list.stream().map(SysMenu::getParentId).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(pidList)) {
      parentLis.addAll(list);
      List<SysMenu> prevList = all.stream().filter(a -> pidList.contains(a.getMenuId())).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(prevList)) {
        parentLis.addAll(prevList);
        getParentIdList(all, prevList, parentLis);
      }
    }

  }

  private void getParentId(Map<Long, Long> allMp, Map<Long, Long> srcMp, Map<Long, Long> map) {
    Map<Long, Long> next = new HashMap<>();
    for (Map.Entry<Long, Long> entry : srcMp.entrySet()) {
      Long val = entry.getValue();
      if (allMp.containsKey(val)) {
        Long pval = MapUtils.getLong(allMp, val);
        if (null != pval) {
          next.put(val, pval);
        }
      }
    }
    if (MapUtils.isNotEmpty(next)) {
      map.putAll(next);
      getParentId(allMp, next, map);
    }

  }


  public void convertData(SysMenu entity) {
    if (null != entity) {
      Long paerentId = entity.getParentId();
      SysMenu parent = null == paerentId ? null : baseDao.get(paerentId);
      entity.setParentName(null == parent ? "" : parent.getMenuName());

    }
  }

  public void convertCollection(List<SysMenu> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> parentId = list.stream().filter(t -> validParentId(t)).map(SysMenu::getParentId).collect(Collectors.toList());
      List<SysMenu> menuList = baseDao.findInMenuId(parentId);
      list.stream().forEach(t -> {
        String parentName = CollectionUtils.isEmpty(menuList) ? "N/A" : menuList.stream().filter(m -> t.getParentId().equals(m.getMenuId())).map(SysMenu::getMenuName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        t.setParentName(parentName);
      });
    }
  }

  public boolean validParentId(SysMenu menu) {
    if (null == menu)
      return false;
    if (null == menu.getParentId())
      return false;
    if (DataConstant.MENU_ROOT.equals(menu.getParentId()))
      return false;
    return true;
  }


}
