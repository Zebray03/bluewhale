package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.po.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class StoreVO {

    private Integer id;

    @NotNull(message = "store name not null")
    private String name;

    @NotNull(message = "图片 不为空")
    private String logoUrl;

    private Double rating;

    private Integer number;

    @NotNull(message = "商店地址不为空")
    private    String location;

    public Store toPO(){
        Store store=new Store();
        store.setId(this.id);
        store.setLocation(this.location);
        store.setLogoUrl(this.logoUrl);
        store.setName(this.name);
        store.setNumber(this.number);
        store.setRating(this.rating);
        return store;
    }
}
