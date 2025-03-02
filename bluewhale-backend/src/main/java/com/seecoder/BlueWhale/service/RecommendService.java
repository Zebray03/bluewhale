package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.util.recommend.dto.RelateDTO;
import com.seecoder.BlueWhale.vo.ProductVO;
import java.util.List;

public interface RecommendService {
    public List<ProductVO>userCfRecommend(int userId);

    public List<ProductVO>productCfRecommend(int productId);

}
