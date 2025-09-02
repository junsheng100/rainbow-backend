package com.rainbow.base.model.base;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * @ClassName OrderModel
 * @Description TODO
 * @Author shijun.liu
 * @Date
 * @Version 1.0
 */
@Data
public class OrderModel implements Serializable {
	private int index ;
	private Sort.Order order;
	
	public OrderModel(){}
	
	public OrderModel(int index, Sort.Order order){
		this.index = index;
		this.order = order;
	}
}
