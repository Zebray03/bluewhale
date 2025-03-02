package com.seecoder.BlueWhale.util.recommend.core;


import com.seecoder.BlueWhale.util.recommend.dto.RelateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 核心算法
 *
 * @author tarzan
 * @version 1.0
 * @date 2020/7/31$ 15:21$
 * @since JDK1.8
 */
@Component
public class UserCF {

    @Autowired
    CoreMath coreMath;
    /**
     * 方法描述: 推荐电影id列表
     *
     * @param userId 当前用户
     * @param list 用户电影评分数据
     * @return {@link List<Integer>}
     * @date 2023年02月02日 14:51:42
     */
    public  List<Integer> recommend(Integer userId, List<RelateDTO> list) {
        //按用户分组
        Map<Integer, List<RelateDTO>>  userMap=list.stream().collect(Collectors.groupingBy(RelateDTO::getUseId));
        //获取其他用户与当前用户的关系值
        Map<Integer,Double>  userDisMap = coreMath.computeNeighbor(userId, userMap,0);
        //获取关系最近的用户
        //新用户
        if(userDisMap.isEmpty()){
            return  null;
        }
        double maxValue=Collections.max(userDisMap.values());
        Set<Integer> userIds=userDisMap.entrySet().stream().filter(e->e.getValue()==maxValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        //取关系最近的用户
        Integer nearestUserId = userIds.stream().findAny().orElse(null);
        System.out.println(nearestUserId);
        if(nearestUserId==null){
            return Collections.emptyList();
        }
        //最近邻用户看过电影列表
        List<Integer>  neighborItems = userMap.get(nearestUserId).stream().map(RelateDTO::getItemId).collect(Collectors.toList());
        //指定用户看过电影列表
        List<Integer>  userItems  = userMap.get(userId).stream().map(RelateDTO::getItemId).collect(Collectors.toList());
        //找到最近邻看过，但是该用户没看过的电影
        neighborItems.removeAll(userItems);
        return neighborItems;
    }

}
