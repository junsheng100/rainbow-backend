package com.rainbow.plans.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 进度更新请求
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "进度更新请求")
public class ProgressUpdateRequest {

    @NotNull(message = "进度不能为空")
    @DecimalMin(value = "0.00", message = "进度不能小于0")
    @DecimalMax(value = "100.00", message = "进度不能大于100")
    @Schema(title = "进度百分比", required = true, example = "75.50")
    private BigDecimal progress;
    
    // Getter和Setter方法
    public BigDecimal getProgress() { return progress; }
    public void setProgress(BigDecimal progress) { this.progress = progress; }
}
