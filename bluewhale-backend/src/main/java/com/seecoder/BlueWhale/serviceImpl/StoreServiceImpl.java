package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.service.StoreService;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {
    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    StoreRepository storeRepository;

    @Override
    public Boolean create(StoreVO storeVO) {
        Store store = storeRepository.findByName(storeVO.getName());
        if (store != null) {
            throw BlueWhaleException.nameAlreadyExists();
        }
        Store newStore = storeVO.toPO();
        newStore.setRating(0.0);
        newStore.setNumber(0);
        storeRepository.save(newStore);
        return true;
    }

    @Override
    public StoreVO getStore(Integer id) {
        Store store = storeRepository.findById(id).orElse(null);
        if (store == null) {
            throw BlueWhaleException.storeNotExists();
        }
        return store.toVO();
    }
    @Override
    public List<StoreVO> getAllStores() {

        log.info("getAllStores start");
        return storeRepository.findAll().stream().map(Store::toVO).collect(Collectors.toList());
    }
}
