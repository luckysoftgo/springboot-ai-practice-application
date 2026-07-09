package com.application.ai.practice.service;


import com.application.ai.practice.model.entity.ReservationInfo;
import com.application.ai.practice.mapper.ReservationInfoMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationInfoMapper reservationInfoMapper;

    //1.添加预约信息的方法
    public void insert(ReservationInfo reservationInfo) {
        reservationInfoMapper.insert(reservationInfo);
    }

    //2.查询预约信息的方法(根据手机号查询)
    public ReservationInfo findByPhone(String phone) {
        List<ReservationInfo> list = reservationInfoMapper.findByPhone(phone);
        if (CollectionUtils.isEmpty(list)){
            return new ReservationInfo();
        }
        return list.get(0);
    }

}
