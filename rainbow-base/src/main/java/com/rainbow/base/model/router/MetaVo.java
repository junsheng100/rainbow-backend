package com.rainbow.base.model.router;


import com.rainbow.base.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 路由显示信息
 *
 * @author rainvom
 */
@Getter
@Setter
public class MetaVo {

  @Schema(title = "标题", type = "String")
  private String title;


  @Schema(title = "路由图标", type = "String")
  private String icon;


  @Schema(title = "是否缓存", type = "String")
  private boolean noCache;


  @Schema(title = "内链地址", type = "String")
  private String link="";

  public MetaVo() {
  }

  public MetaVo(String title, String icon) {
    this.title = title;
    this.icon = icon;
  }

  public MetaVo(String title, String icon, boolean noCache) {
    this.title = title;
    this.icon = icon;
    this.noCache = noCache;
  }

  public MetaVo(String title, String icon, String link) {
    this.title = title;
    this.icon = icon;
    this.link = link;
  }

  public MetaVo(String title, String icon, boolean noCache, String link) {
    this.title = title;
    this.icon = icon;
    this.noCache = noCache;
    if (StringUtils.ishttp(link)) {
      this.link = link;
    }
  }

}
