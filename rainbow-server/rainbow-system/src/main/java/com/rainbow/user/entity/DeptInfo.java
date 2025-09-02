package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author rainvom
 */
@Getter
@Setter
@Entity
@Table(name = "dept_info")
@org.hibernate.annotations.Table(appliesTo = "dept_info", comment = "部门")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class DeptInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 20)
    @Schema(title = "部门ID",type = "Long")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;


    @Column(length = 20)
    @Schema(title = "上级部门ID",type = "Long")
    @OrderBy(value = Sort.Direction.ASC,INDEX="99")
    private Long parentId;


    @Column(length = 32)
    @Schema(title = "部门名称",type = "String")
    @Search(SELECT = SearchEnum.LIKE)
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    @Column(length = 11)
    @NotNull(message = "显示顺序不能为空")
    @Schema(title = "显示顺序",type = "Integer")
    @OrderBy(value = Sort.Direction.ASC,INDEX="100")
    private Integer orderNum;


    @Column(length = 32)
    @Schema(title = "联系人",type = "String")
    private String leader;


    @Column(length = 16)
    @Schema(title = "联系人",type = "String")
    private String phone;


    @Column
    @Schema(title = "简介",type = "String")
    private String overview;

    @Column(length = 128)
    @Schema(title = "邮箱",type = "String")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Transient
    @Schema(title = "上级部门名称",type = "String")
    private String parentName;




    @Schema(title = "子部门",type = "List")
    @Transient
    private List<DeptInfo> children ;



}
