package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.CouponVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Coupon {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "group_id")
    private Integer groupId;

    @Basic
    @Column(name = "used")
    private Boolean used;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
    private CouponGroup couponGroup;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    public CouponVO toVO(){
        CouponVO couponVO=new CouponVO();
        couponVO.setId(this.id);
        couponVO.setUsed(this.used);
        couponVO.setUserId(this.userId);
        couponVO.setGroupId(this.groupId);
        return couponVO;
    }
}
