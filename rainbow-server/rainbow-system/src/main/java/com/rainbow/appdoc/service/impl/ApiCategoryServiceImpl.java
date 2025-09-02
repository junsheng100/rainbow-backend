package com.rainbow.appdoc.service.impl;

import com.rainbow.SystemApplication;
import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.appdoc.entity.AppInterface;
import com.rainbow.appdoc.model.CategoryModel;
import com.rainbow.appdoc.model.InterfaceModel;
import com.rainbow.appdoc.resource.ApiCategoryDao;
import com.rainbow.appdoc.resource.ApiInterfaceDao;
import com.rainbow.appdoc.resource.ApiModelTypeDao;
import com.rainbow.appdoc.resource.ApiReferenceDao;
import com.rainbow.appdoc.service.ApiCategoryService;
import com.rainbow.appdoc.utils.ApiUtils;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.resource.SysMenuDao;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ApiCategory Service实现
 */
@Slf4j
@Service
public class ApiCategoryServiceImpl extends BaseServiceImpl<AppCategory, String, ApiCategoryDao> implements ApiCategoryService {

  @Autowired
  private ApiInterfaceDao interfaceDao;
  @Autowired
  private ApiModelTypeDao modelTypeDao;
  @Autowired
  private ApiReferenceDao referenceDao;
  @Autowired
  private SysMenuDao menuDao;

  
  @Override
  public Boolean init() {

    List<Class<?>> classList = getAppCategoryClassList();
    List<AppCategory> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(classList)) {
      for (Class<?> clzz : classList) {
        String className = clzz.getName();
        AppCategory category = create(className);
        list.add(category);
      }

      List<String> idList = list.stream().map(AppCategory::getId).collect(Collectors.toList());

      List<String> interfaceIdList = new ArrayList<>();
      list.stream().forEach(category -> {
        List<AppInterface> interfaces = category.getInterfaces();
        if (CollectionUtils.isNotEmpty(interfaces)) {
          List<String> interfaceIds = interfaces.stream().map(item -> item.getId()).collect(Collectors.toList());
          interfaceIdList.addAll(interfaceIds);
        }
      });

      List<AppCategory> leftCategory = baseDao.findNotInId(idList);
      List<AppInterface> leftInterface = interfaceDao.findNotInId(interfaceIdList);
      List<SysMenu> menuList = menuDao.findNotInInterfaceId(interfaceIdList);

      if (CollectionUtils.isNotEmpty(leftCategory)) {
        for (AppCategory item : leftCategory) {
          baseDao.remove(item.getId());
        }
      }

      if (CollectionUtils.isNotEmpty(leftInterface)) {
        for (AppInterface item : leftInterface) {
          interfaceDao.remove(item.getId());
        }
      }

      if (CollectionUtils.isNotEmpty(menuList)) {
        for (SysMenu menu : menuList) {
          menu.setRequestUrl("");
          menu.setRequestMethod("");
          menu.setInterfaceId("");
          menuDao.save(menu);
        }
      }

    }
    return true;
  }


//  @PostConstruct
  @Override
  public void initData() {

    Thread thread = new Thread(() -> {
      init();
    });
    thread.start();
  }

  
  @Transient
  @Override
  public AppCategory create(String className) {

    Class clzz = null;
    try {
      if (StringUtils.isBlank(className))
        throw new BizException("请输入类名");

      List<String> classNameList = getAppCategoryClassNameList();

      if (CollectionUtils.isEmpty(classNameList))
        return null;

      String fullClassName = classNameList.stream().filter(item -> item.endsWith(className)).findFirst().orElse(null);

      if (StringUtils.isBlank(fullClassName))
        throw new BizException("API 类不存在");

      clzz = Class.forName(fullClassName);

      RequestMapping requestMapping = (RequestMapping) clzz.getAnnotation(RequestMapping.class);

      if (requestMapping == null)
        throw new BizException("请检查类是否正确URL请求");
      AppCategory appCategory = ApiUtils.getAppCategory(clzz);

      baseDao.save(appCategory);
      String categoryId = appCategory.getId();
      List<AppInterface> appInterfaces = ApiUtils.getAppInterface(clzz);

      if (CollectionUtils.isNotEmpty(appInterfaces)) {
        List<AppInterface> interfaces = new ArrayList<>();
        for (AppInterface method : appInterfaces) {
          method.setCategoryId(categoryId);
          interfaceDao.save(method);
//          createDataType(className,method);
          interfaces.add(method);
        }
        appCategory.setInterfaces(interfaces);
      }

      return appCategory;
    } catch (Exception e) {
      e.printStackTrace();
      throw new BizException(e.getMessage());
    }

  }


  private List<String> getAppCategoryClassNameList() {
    List<Class<?>> classList = getAppCategoryClassList();

    return classList.stream().map(item -> item.getName()).collect(Collectors.toList());
  }


  @SneakyThrows
  public List<Class<?>> getAppCategoryClassList() {

    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
    Class<?> clazz = SystemApplication.class;
    ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
    String[] packages = componentScan.basePackages();

    provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
      return metadataReader.getAnnotationMetadata().hasAnnotation(RestController.class.getName())
              || metadataReader.getAnnotationMetadata().hasAnnotation(Controller.class.getName());
    });

    List<Class<?>> list = new ArrayList<>();
    for (String pkg : packages) {
      Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(pkg);

      for (BeanDefinition beanDefinition : beanDefinitions) {
        String className = beanDefinition.getBeanClassName();

        Class clzz = Class.forName(className);
        RequestMapping requestMapping = (RequestMapping) clzz.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
          list.add(clzz);
        }
      }
    }
    return list;
  }


  @Override
  public PageData<AppCategory> findByKeyword(CommonVo<String> vo) {

    return baseDao.findByKeyword(vo);
  }

  @Override
  public PageData<CategoryModel> findMenus(CommonVo<String> vo) {

    PageData<AppCategory> pageData = baseDao.findByKeyword(vo);

    Pageable pageable = pageData.getPageable();

    List<AppCategory> list = pageData.getContent();
    if (CollectionUtils.isEmpty(list)) {
      return new PageData<>();
    }
    List<String> categoryIds = list.stream().map(AppCategory::getId).collect(Collectors.toList());

    List<AppInterface> interfaces = interfaceDao.findInCategoryIds(categoryIds);
    List<CategoryModel> modelList = new ArrayList<>();

    for (AppCategory category : pageData.getContent()) {
      CategoryModel model = new CategoryModel();
      BeanUtils.copyProperties(category, model, CommonUtils.getNullPropertyNames(category));

      List<AppInterface> categoryInterfaces = interfaces.stream()
              .filter(item -> item.getCategoryId().equals(category.getId()))
              .collect(Collectors.toList());

      List<InterfaceModel> interfaceModelList = categoryInterfaces.stream().map(item -> {
        InterfaceModel interfaceModel = new InterfaceModel();
        BeanUtils.copyProperties(item, interfaceModel, CommonUtils.getNullPropertyNames(item));
        return interfaceModel;
      }).collect(Collectors.toList());

      model.setInterfaceModelList(interfaceModelList);

      modelList.add(model);
    }
    PageData<CategoryModel> page = new PageData<>(modelList, pageable, pageData.getTotal(), pageData.getPages());
    return page;
  }





}