package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 *
 * @author rainvom
 */
@Data
@Entity
@Table(name = "sys_config")
@org.hibernate.annotations.Table(appliesTo = "sys_config", comment = "系统设置表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @Id
    @Schema(title = "主键",type = "Long")
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    /**
     * 参数名称
     */
    @Column(length = 100)
    @Schema(title = "参数名称",type = "String")
    @NotBlank(message = "参数名称不能为空")
    @Search(SELECT = SearchEnum.LIKE)
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /**
     * 参数键名
     */
    @UnionKey
    @Column(length = 100)
    @Schema(title = "参数键",type = "String")
    @NotBlank(message = "参数键长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /**
     * 参数键值
     */
    @Column(length = 2000)
    @Schema(title = "参数值",type = "String")
    @NotBlank(message = "参数键值不能为空")
    @Search(SELECT = SearchEnum.LIKE)
    @Size(min = 0, max = 2000, message = "参数键值长度不能超过2000个字符")
    private String configValue;

    @Schema(title = "显示顺序",type = "Integer")
    @OrderBy(value = Sort.Direction.ASC,INDEX="99")
    @Column(length = 11)
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;





    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("configId", getConfigId())
                .append("configName", getConfigName())
                .append("configKey", getConfigKey())
                .append("configValue", getConfigValue())
                .toString();
    }
}
