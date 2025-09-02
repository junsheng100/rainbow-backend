package com.rainbow.base.model.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author rainvom
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {

  @Schema(title = "ID", type = "Long")
  private Long id;

  @Schema(title = "上级 ID", type = "Long")
  private Long parentId;

  @Schema(title = "路由名字", type = "String")
  private String name;

  @Schema(title = "路由地址", type = "String")
  private String path;

  @Schema(title = "是否隐藏", type = "boolean")
  private boolean hidden;

  /*当设置 noRedirect 的时候该路由在面包屑导航中不可被点击*/
  @Schema(title = "重定向地址", type = "String")
  private String redirect;

  @Schema(title = "组件地址", type = "String")
  private String component;

  @Schema(title = "路由参数，JSON 格式", type = "String")
  private String query;

  /* 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面 */
  @Schema(title = "路径声明", type = "String")
  private Boolean alwaysShow;


  @Schema(title = "其他元素", type = "MetaVo")
  private MetaVo meta;

  @Schema(title = "排序", type = "Integer")
  private Integer orderNum;

  @Schema(title = "层级", type = "Integer")
  private Integer orderLevel;

  @Schema(title = "子路由", type = "List")
  private List<RouterVo> children;


}
