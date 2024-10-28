package com.jing.msc.cobweb;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.log.aspect.WebLog;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : jing
 * @since : 2024/9/3 13:45
 */
@RestController
@RequestMapping("/sso")
public class SsoController {

    @WebLog(description = "sso/auth")
    @Operation(summary = "sso/auth")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/auth")
    public void auth(@RequestParam(value = "redirect", required = false) String redirect, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(redirect + "&ticket=ticket");
    }

    @WebLog(description = "sso/checkTicket")
    @Operation(summary = "sso/checkTicket")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping("/checkTicket")
    public Map<String, Object> checkTicket(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("code", 200);
        params.put("data", "393916541915668821");
        params.put("idCard", "210105198712103757");
        return params;
    }


}
