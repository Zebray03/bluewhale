package com.seecoder.BlueWhale.service;

import java.util.List;

import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.ProductSearchVO;
import com.seecoder.BlueWhale.vo.ProductVO;

public interface ProductService {
    Boolean create(ProductVO productVO);

    Boolean addStock(Integer id, Integer number);

    List<ProductVO> getAllProducts(Integer storeId);

    ProductVO getProduct(Integer id);

    ProductSearchVO getProductsWithCondition(ProductSearchVO productSearchVO);

    List<CommentVO> getComments(Integer productId);
}
