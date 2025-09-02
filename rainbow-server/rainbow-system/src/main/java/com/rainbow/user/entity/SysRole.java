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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色（Role）
 * 指个体在特定情境或任务中承担的功能或行为期望，更强调动态的责任和互动关系。
 * 例如：项目经理（角色）可能由技术主管（岗位）兼任；或某员工在会议中扮演"协调者"角色。
 * 特点是动态的，可能跨越岗位边界，随任务或团队需求变化。
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role")
@org.hibernate.annotations.Table(appliesTo = "sys_role", comment = "角色信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class SysRole extends BaseEntity {

    @Id
    @Schema(title = "角色ID",type = "Long")
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;


    @Column(length = 50)
    @Schema(title = "角色名称",type = "String")
    @Search(SELECT = SearchEnum.LIKE)
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;


    @Column(length = 100)
    @Schema(title = "角色权限",type = "String")
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;


    @Column(length = 11)
    @Schema(title = "角色排序",type = "Integer")
    @OrderBy(value = Sort.Direction.ASC,INDEX="99")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    @Schema(title = "数据范围",type = "String")
    @Column(columnDefinition = "varchar(2) comment '1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限'")
    private String dataScope;


    @Column(columnDefinition = "varchar(2) comment '0代表存在 1代表删除'")
    private String delFlag;

    @Transient
    private List<Long> rightIdList;




    public SysRole() {

    }
    public SysRole(Long roleId) {
        this.roleId = roleId;
    }



}
