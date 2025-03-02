package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.CouponTypeEnum;
import com.seecoder.BlueWhale.po.CouponGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CouponGroupVO {

    private Integer id;

    @NotNull(message = "优惠券类型不为空")
    private CouponTypeEnum type;

    private Integer satisfaction;

    private Integer minus;

    @NotNull(message = "优惠券使用范围不为空")
    private Integer storeId;

    @NotNull(message = "剩余数量不为空")
    @Range(min = 1,message = "优惠券数量为正数")
    private Integer rest;

    private String storeName;

    public CouponGroup toPO(){
        CouponGroup couponGroup=new CouponGroup();
        couponGroup.setId(this.id);
        couponGroup.setRest(this.rest);
        couponGroup.setMinus(this.minus);
        couponGroup.setType(this.type);
        couponGroup.setSatisfaction(this.satisfaction);
        couponGroup.setStoreId(this.storeId);
        return couponGroup;
    }
}
