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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dict_type")
@org.hibernate.annotations.Table(appliesTo = "sys_dict_type", comment = "字典数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Id
    @Schema(title = "字典主键",type = "Long")
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictId;

    @Column(length = 100)
    @Schema(title = "字典名称",type = "String")
    @Search(SELECT = SearchEnum.LIKE)
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    @Column(columnDefinition = "varchar(2) default '0'")
    @Schema(title = "状态（0:可用,-1:禁用）",type = "String")
    private String disabled;

    @UnionKey
    @Column(length = 100,unique = true)
    @Schema(title = "字典类型",type = "String")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String dictType;

    @Schema(title = "显示顺序",type = "Integer")
    @OrderBy(value = Sort.Direction.ASC,INDEX="100")
    @Column(length = 11)
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    @Schema(title = "备注",type = "String")
    @Column
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String remark;



}
