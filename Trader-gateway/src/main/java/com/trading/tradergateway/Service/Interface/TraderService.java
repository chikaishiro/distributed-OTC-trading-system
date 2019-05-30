package com.trading.tradergateway.Service.Interface;


import com.trading.tradergateway.Entity.Trader;
import java.util.List;

public interface TraderService {
    /**
     * 所有用户信息
     * @return trader列表
     */
    List findAllTraders();

    /**
     * 检查用户是否存在
     * @param trader 新用户
     * @return 布尔值
     */
    boolean checkDuplicate(Trader trader);

    /**
     * 用户注册
     * @param trader 新用户
     * @return trader信息
     */
    Trader register(Trader trader);

    /**
     * 用户登录
     * @param traderName 用户名
     * @param password 密码
     * @return token字符串
     */
    String login(String traderName, String password);

    /**
     * 刷新Token
     * @param oldToken 旧token
     * @return 新token字符串
     */
    String refreshToken(String oldToken);
}