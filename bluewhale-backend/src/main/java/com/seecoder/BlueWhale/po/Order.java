package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.enums.OrderTypeEnum;
import com.seecoder.BlueWhale.vo.OrderVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`order`") //order是数据库保留字段，使用要加引号
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "product_id")
    private Integer productId;

    @Basic
    //@NotNull
    @Column(name = "amount")
    private Integer amount;

    @Basic
    //@NotNull
    @Column(name = "paid")
    private Double paid;

    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrderTypeEnum type;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "rating")
    private Integer rating;

    @Basic
    //@NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Basic
    //@NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    //@NotNull
    @Column(name = "finish_time")
    private Date finishTime;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    public OrderVO toVO() {
        OrderVO orderVO = new OrderVO();
        orderVO.setAddress(this.address);
        orderVO.setAmount(this.amount);
        orderVO.setContent(this.content);
        orderVO.setCreateTime(this.createTime);
        orderVO.setFinishTime(this.finishTime);
        orderVO.setId(this.id);
        orderVO.setPaid(this.paid);
        orderVO.setPhone(this.phone);
        orderVO.setProductId(this.productId);
        orderVO.setRating(this.rating);
        orderVO.setStatus(this.status);
        orderVO.setType(this.type);
        orderVO.setUserId(this.userId);
        return orderVO;
    }
}
