package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wall_paper")
@Schema(title = "壁纸")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class Wallpaper extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(title = "主键ID", type = "Long")
  private Long id;

  @Schema(title = "开始日期(YYYYMMDD)", type = "String")
  @Column(name = "start_date", length = 8)
  private String startDate;

  @Schema(title = "完整开始日期时间", type = "String")
  @Column(name = "full_start_date", length = 12)
  private String fullStartDate;

  @Schema(title = "结束日期(YYYYMMDD)", type = "String")
  @Column(name = "end_date", length = 8)
  private String endDate;

  @Schema(title = "壁纸相对URL", type = "String")
  @Column(name = "url", length = 500)
  private String url;

  @Schema(title = "壁纸基础URL", type = "String")
  @Column(name = "url_base", length = 500)
  private String urlBase;

  @Schema(title = "完整壁纸URL", type = "String")
  @Column(name = "full_url", length = 1000)
  private String fullUrl;

  @Schema(title = "本地存储路径", type = "String")
  @Column(name = "local_path", length = 500)
  private String localPath;

  @Schema(title = "图片信息", type = "String")
  @Column(name = "copyright", length = 1000)
  private String copyright;

  @Schema(title = "版权链接", type = "String")
  @Column(name = "copyright_link", length = 500)
  private String copyrightLink;

  @Schema(title = "标题", type = "String")
  @Column(name = "title", length = 200)
  private String title;

  @Schema(title = "测验链接", type = "String")
  @Column(name = "quiz", length = 500)
  private String quiz;

  @Schema(title = "是否可用作壁纸", type = "Boolean")
  @Column(name = "wp")
  private Boolean wp;

  @Schema(title = "哈希值", type = "String")
  @Column(name = "hsh", length = 64)
  private String hsh;

  @Schema(title = "深色模式", type = "Integer")
  @Column(name = "drk")
  private Integer drk;

  @Schema(title = "顶部标记", type = "Integer")
  @Column(name = "top")
  private Integer top;

  @Schema(title = "底部标记", type = "Integer")
  @Column(name = "bot")
  private Integer bot;

  @Schema(title = "文件大小(字节)", type = "Long")
  @Column(name = "file_size")
  private Long fileSize;

  @Schema(title = "下载状态(0:未下载,1:已下载,2:下载失败)", type = "Integer")
  @Column(name = "download_status")
  private Integer downloadStatus;

  @Schema(title = "下载时间", type = "LocalDateTime")
  @Column(name = "download_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime downloadTime;

  @Schema(title = "查看次数", type = "Integer")
  @Column(name = "view_count")
  private Integer viewCount;

  @Schema(title = "下载次数", type = "Integer")
  @Column(name = "download_count")
  private Integer downloadCount;

  @Schema(title = "是否精选", type = "Boolean")
  @Column(name = "is_featured")
  private Boolean isFeatured;

  @Schema(title = "是否登录页", type = "Boolean")
  @Column(name = "is_login")
  private Boolean isLogin;

  @Schema(title = "是否工作时背景", type = "Boolean")
  @Column(name = "is_work")
  private Boolean isWork;

  @Column(length = 11)
  @Schema(title = "显示顺序", type = "Integer")
  @OrderBy(value = Sort.Direction.ASC, INDEX = "2")
  @NotNull(message = "显示顺序不能为空")
  private Integer orderNum;

  public Wallpaper() {
  }





}