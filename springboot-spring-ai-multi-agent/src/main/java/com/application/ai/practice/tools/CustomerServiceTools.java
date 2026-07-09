package com.application.ai.practice.tools;

import com.application.ai.practice.model.OrderInfoStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 客服业务工具集
 *
 * 使用 @Tool 注解将方法暴露给 AI Agent 调用。
 * Agent 会根据工具描述自动决定何时调用哪个工具。
 */
@Component
public class CustomerServiceTools {

    /**
     * 查询订单状态
     */
    @Tool(name = "queryOrderStatus", description = "根据订单号查询订单当前状态和物流信息")
    public OrderInfoStatus queryOrderStatus(
            @ToolParam(description = "订单号，通常是12位数字") String orderId) {
        // 实际项目中接入订单微服务
        // 这里用模拟数据演示
        System.out.printf("[工具调用] queryOrderStatus: orderId=%s%n", orderId);
        return new OrderInfoStatus(
                orderId,
                "已发货",
                "顺丰速运",
                "SF1234567890",
                LocalDateTime.now().minusDays(1)
        );
    }

    /**
     * 创建售后工单
     */
    @Tool(name = "createTicket", description = "为用户创建售后工单，返回工单编号")
    public String createTicket(
            @ToolParam(description = "用户ID") String userId,
            @ToolParam(description = "问题类型：HARDWARE/SOFTWARE/RETURN/OTHER") String type,
            @ToolParam(description = "问题描述") String description) {
        String ticketId = "TKT-" + System.currentTimeMillis();
        System.out.printf("[工具调用] createTicket: userId=%s, type=%s, ticketId=%s%n",
                userId, type, ticketId);
        return ticketId;
    }

    /**
     * 搜索 FAQ 知识库
     */
    @Tool(name = "searchFAQ", description = "搜索知识库中的常见问题解答")
    public String searchFAQ(
            @ToolParam(description = "搜索关键词") String keyword) {
        System.out.printf("[工具调用] searchFAQ: keyword=%s%n", keyword);

        Map<String, String> faqDb = Map.of(
                "充电", "请使用原装充电器，检查充电口是否有异物。若仍无法充电，建议：1.更换充电线测试；2.重启设备；3.联系售后检测电池。",
                "闪退", "请升级到最新版本App（v3.2.1），并清理缓存：设置→应用管理→清除缓存。若仍闪退，请反馈您的设备型号和系统版本。",
                "退货", "支持7天无理由退货，商品需保持原包装完好。退货流程：在线申请→寄回商品→3-5个工作日退款。",
                "保修", "硬件享受1年免费保修服务，电池6个月。人为损坏、进液、改装不在保修范围内。",
                "发票", "可申请增值税电子普通发票，购买后7天内在订单详情页申请。"
        );

        for (Map.Entry<String, String> entry : faqDb.entrySet()) {
            if (keyword.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "未在知识库中找到相关问题，建议联系人工客服获取专业帮助。";
    }

    /**
     * 查询促销活动
     */
    @Tool(name = "queryPromotion", description = "查询当前有效的促销活动和优惠信息")
    public String queryPromotion(
            @ToolParam(description = "产品类别，如：手机、耳机、平板等") String category) {
        System.out.printf("[工具调用] queryPromotion: category=%s%n", category);
        return String.format("当前%s类产品促销活动：满2000减200，限时48小时；以旧换新最高抵扣500元；学生认证享9折优惠。", category);
    }
}
