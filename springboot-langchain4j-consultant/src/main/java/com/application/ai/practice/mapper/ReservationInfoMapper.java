package com.application.ai.practice.mapper;

import com.application.ai.practice.model.entity.ReservationInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReservationInfoMapper {

    /**
     * 添加预约信息
     * @param reservationInfo
     */

    @Insert("insert into reservation_info (reservation_name,reservation_gender,reservation_phone,communication_time,reservation_province,estimated_score,reservation_desc) values (#{reservationName},#{reservationGender},#{reservationPhone},#{communicationTime},#{reservationProvince},#{estimatedScore},#{reservationDesc})")
    void insert(ReservationInfo reservationInfo);

    /**
     * 根据手机号查询预约信息
     * @param phone
     * @return
     */
    @Select(" select * from reservation_info where reservation_phone = #{phone} ")
    List<ReservationInfo> findByPhone(String phone);

}
