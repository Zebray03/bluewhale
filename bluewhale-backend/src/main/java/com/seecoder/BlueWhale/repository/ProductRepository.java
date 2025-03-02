package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByStoreId(Integer storeId);

    Product findByStoreIdAndName(Integer storeId,String name);
}
