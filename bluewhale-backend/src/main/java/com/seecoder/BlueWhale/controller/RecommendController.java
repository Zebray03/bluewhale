package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.service.RecommendService;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.ProductVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private SecurityUtil securityUtil;
    @GetMapping("/product/{id}")
    public ResultVO<List<ProductVO>> getRecommendProduct(@PathVariable(value = "id") Integer id){

        return ResultVO.buildSuccess(recommendService.productCfRecommend(id));
    }
    @GetMapping()
    public ResultVO<List<ProductVO>> getRecommendUser(){
        Integer id=securityUtil.getCurrentUser().getId();
        return ResultVO.buildSuccess(recommendService.userCfRecommend(id));
    }
}
