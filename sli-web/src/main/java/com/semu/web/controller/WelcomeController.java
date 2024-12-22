package com.semu.web.controller;

import com.sli.common.model.dto.*;
import com.sli.common.result.ResultModel;
import com.sli.common.util.DateUtil;
import com.sli.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeController {
    @Autowired
    private SliVisitLogService sliVisitLogService;
    @Autowired
    private ChinaHolidayService chinaHolidayService;
    @Autowired
    private PoetryService poetryService;
    @Autowired
    private SliSysConfigService sliSysConfigService;
    @Autowired
    private SentenceService sentenceService;

    @GetMapping("/queryVisitorCount")
    public ResultModel<VisitorCountDTO> queryVisitorCount() {
        Date startTime = DateUtil.getStartOfDay(Calendar.getInstance().getTime());
        Date endTime = DateUtil.getEndOfDay(Calendar.getInstance().getTime());
        return ResultModel.success(sliVisitLogService.queryVisitorCount(startTime, endTime));
    }


    @GetMapping("/queryPoetry")
    public ResultModel<PoetryDTO> queryPoetry() {
        return ResultModel.success(poetryService.queryPoetry());
    }


    @GetMapping("/queryHoliday")
    public ResultModel<List<ChinaHolidayDTO>> queryHoliday(@RequestParam String month) {
        return ResultModel.success(chinaHolidayService.queryHoliday(month));
    }

    @GetMapping("/querySkylineWebcams")
    public ResultModel<List<SkylineWebcamsDTO>> querySkylineWebcams() {
        return ResultModel.success(sliSysConfigService.querySkylineWebcams());
    }

    @GetMapping("/querySentence")
    public ResultModel<SentenceDTO> querySentence() {
        return ResultModel.success(sentenceService.querySentence());
    }

}
