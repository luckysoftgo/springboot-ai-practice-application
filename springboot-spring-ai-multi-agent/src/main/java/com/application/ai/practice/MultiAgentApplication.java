package com.application.ai.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring AI Multi-Agent 智能客服系统启动类
 *
 * 架构说明：
 *   Orchestrator-Workers 模式
 *   - OrchestratorAgent：调度Agent，负责拆解用户请求为子任务
 *   - TechSupportAgent：技术支持Worker，处理硬件/设备问题
 *   - ProductInfoAgent：产品Worker，处理软件Bug/功能建议
 *   - SalesInfoAgent：销售Worker，处理价格/购买咨询
 *   - SummaryInfoAgent：总结Agent，整合所有Worker输出并生成最终回复
 *
 * 运行方式：
 *   export OPENAI_API_KEY="sk-xxxxxxxx"
 *   ./mvnw spring-boot:run
 */

@SpringBootApplication
public class MultiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiAgentApplication.class, args);
    }
}
