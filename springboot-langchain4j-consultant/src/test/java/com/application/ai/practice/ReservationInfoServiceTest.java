package com.application.ai.practice;

import com.application.ai.practice.model.entity.ReservationInfo;
import com.application.ai.practice.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ReservationInfoServiceTest {

    @Autowired
    private ReservationService reservationService;

    //测试添加
    @Test
    void testInsert(){
        ReservationInfo reservationInfo = new ReservationInfo(null, "小王", "男", "13800000001", LocalDateTime.now(), "上海", 580,"");
        reservationService.insert(reservationInfo);
    }

    //测试查询
    @Test
    void testFindByPhone(){
        String phone = "13800000001";
        ReservationInfo reservationInfo = reservationService.findByPhone(phone);
        System.out.println(reservationInfo);
    }
}
