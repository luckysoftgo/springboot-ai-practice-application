package com.application.ai.practice.tools;

import com.application.ai.practice.model.entity.ReservationInfo;
import com.application.ai.practice.service.ReservationService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定义工具类
 */
@Component
public class ReservationTool {

    @Autowired
    private ReservationService reservationService;

    /**
     * 工具方法: 添加预约信息
     * @param reservationName
     * @param reservationGender
     * @param reservationPhone
     * @param communicationTime
     * @param reservationProvince
     * @param estimatedScore
     * @param reservationDesc
     */
    @Tool(name = "预约志愿填报服务") //方法名作为工具名称
    public void addReservation(
            @P("考生姓名") String reservationName,
            @P("考生性别") String reservationGender,
            @P("考生手机号") String reservationPhone,
            @P("预约沟通时间,格式为: yyyy-MM-dd'T'HH:mm") String communicationTime,
            @P("考生所在省份") String reservationProvince,
            @P("考生预估分数") Integer estimatedScore,
            @P("考生预估描述") String reservationDesc
    ) {
        ReservationInfo reservationInfo = new ReservationInfo(null, reservationName, reservationGender, reservationPhone, LocalDateTime.parse(communicationTime), reservationProvince, estimatedScore,reservationDesc);
        reservationService.insert(reservationInfo);
    }

    /**
     * 工具方法: 查询预约信息
     * @param phone
     * @return
     */
    @Tool(name = "根据考生手机号查询预约单")
    public ReservationInfo findReservation(@P("考生手机号") String phone) {
        return reservationService.findByPhone(phone);
    }
}
