package com.application.ai.practice.model;

import java.time.LocalDateTime;

/**
 * 订单状态信息
 */
public record OrderInfoStatus(
        String orderId,
        String status,
        String carrier,
        String trackingNumber,
        LocalDateTime updateTime
) {}
