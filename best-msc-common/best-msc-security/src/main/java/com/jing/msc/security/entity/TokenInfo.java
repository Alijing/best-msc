package com.jing.msc.security.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : jing
 * @since : 2024/7/5 17:08
 */
@Data
@AllArgsConstructor
@Schema(description = "token信息")
public class TokenInfo {

    @Schema(description = "token")
    private String token;

    @Schema(description = "token过期时间")
    private Integer expireTime;

}
