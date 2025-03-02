package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.CategoryEnum;
import com.seecoder.BlueWhale.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {

    private Integer id;

    @NotNull(message = "商品所属商店不为空")
    private Integer storeId;

    private Double rating;

    private Integer number;

    private List<String> photoUrlList;

    @NotNull(message = "商品名字不为空")
    private String name;

    private Integer salesAmount;

    private Integer stock;

    @NotNull(message = "价格不为空")
    @Range(min = 1,message = "价格大于1")
    private Double price;

    @NotNull(message = "商品品类不为空")
    private CategoryEnum category;

    public Product toPO() {
        Product product = new Product();
        product.setCategory(this.category);
        product.setId(this.id);
        product.setPrice(this.price);
        product.setName(this.name);
        product.setNumber(this.number);
        product.setRating(this.rating);
        product.setStock(this.stock);
        product.setStoreId(this.storeId);
        product.setSalesAmount(this.salesAmount);
        product.setPhotoUrlList(this.photoUrlList);
        return product;
    }
}
