package com.semu.web.controller;

import com.sli.aspect.visit.VisitLog;
import com.sli.common.limit.slidingwindow.SlidingWindowRateLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {


    @GetMapping("/")
    @VisitLog
    @SlidingWindowRateLimit(requests = 2, timeWindow = 2)
    public String index(HttpServletRequest request) {
        return "index";
    }
}
