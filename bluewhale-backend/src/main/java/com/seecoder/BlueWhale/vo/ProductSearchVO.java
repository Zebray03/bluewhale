package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.CategoryEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ProductSearchVO {
    Integer lowValue;
    Integer highValue;
    String name;
    CategoryEnum category;
    List<ProductVO>REproductList;
    Integer Page_index;
    Integer Page_size;
    Integer Page_num;
}
