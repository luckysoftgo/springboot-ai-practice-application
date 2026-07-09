package com.application.ai.practice.controller;

import com.application.ai.practice.model.ChatRequest;
import com.application.ai.practice.model.CustomerResponse;
import com.application.ai.practice.service.CustomerInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客服 REST API 接口
 *
 * 主接口：
 *   POST /api/customer-service/chat
 *   Body: {"message": "用户消息"}
 *
 * 健康检查：
 *   GET /api/customer-service/health
 */
@RestController
@RequestMapping("/api/customer-service")
@CrossOrigin(origins = "*")
public class CustomerInfoController {

    private final CustomerInfoService customerInfoService;

    public CustomerInfoController(CustomerInfoService customerInfoService) {
        this.customerInfoService = customerInfoService;
    }

    /**
     * 客服对话接口
     *
     * 示例请求：
     * curl -X POST http://localhost:8080/api/customer-service/chat \
     *   -H "Content-Type: application/json" \
     *   -d '{"message":"我买的手机充不进电，而且你们的App总是闪退，你们这是什么质量？"}'
     */
    @PostMapping("/chat")
    public ResponseEntity<CustomerResponse> chat(@RequestBody ChatRequest request) {
        try {
            CustomerResponse response = customerInfoService.handleRequest(request.message());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 出错时返回兜底响应
            CustomerResponse fallback = new CustomerResponse(
                    "非常抱歉，系统暂时遇到了一些问题，请稍后重试或联系人工客服。",
                    java.util.List.of("拨打客服热线: 400-xxx-xxxx"),
                    true
            );
            return ResponseEntity.ok(fallback);
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Multi-Agent Service is running!");
    }

}
