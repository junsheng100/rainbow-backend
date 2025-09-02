package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典数据表 sys_dict_data
 *
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dict_data")
@org.hibernate.annotations.Table(appliesTo = "sys_dict_data", comment = "字典数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysDictData extends BaseEntity {


    @Id
    @Schema(title = "字典编码主键",type = "Long")
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictCode;


    @Column(length = 11)
    @Schema(title = "字典排序",type = "Long")
    @OrderBy(value = Sort.Direction.ASC,INDEX = "99")
    private Long dictSort;

    @Column(length = 100)
    @Schema(title = "字典标签",type = "String")
    @Search(SELECT = SearchEnum.LIKE)
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    @UnionKey
    @Column(length = 100)
    @Schema(title = "字典键值",type = "String")
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    @UnionKey
    @Column(length = 100)
    @Schema(title = "字典类型",type = "String")
    @NotBlank(message = "字典类型不能为空")
    @OrderBy(value = Sort.Direction.ASC,INDEX = "98")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    @Transient
    @Schema(title = "字典名称",type = "String")
    private String dictName;

    @Schema(title = "是否默认（Y是 N否）",type = "String")
    @Column(length = 1)
    private String isDefault;

    @Column(columnDefinition = "varchar(2) default '0'")
    @Schema(title = "状态（0:可用,-1:删除）",type = "String")
    private String disabled;


}
