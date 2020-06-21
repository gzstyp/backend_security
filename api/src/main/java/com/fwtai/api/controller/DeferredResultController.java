package com.fwtai.api.controller;

import com.fwtai.service.api.DeferredResultService;
import com.fwtai.service.api.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;

/**
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-25 10:39
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
@RestController
@RequestMapping("deferred")
public class DeferredResultController{

    @Resource
    private DeferredResultService deferredResultService;

    /**
     * 为了方便测试，简单模拟一个
     * 多个请求用同一个requestId会出问题
     */
    private final String requestId = "haha";

    // http://127.0.0.1:8030/deferred/get?timeout=8000
    @GetMapping(value = "/get")
    public DeferredResult<ResultResponse> get(@RequestParam(value = "timeout", required = false, defaultValue = "10000") Long timeout) {
        DeferredResult<ResultResponse> deferredResult = new DeferredResult<>(timeout);
        deferredResultService.process(requestId, deferredResult);
        return deferredResult;
    }

    /** // http://127.0.0.1:8030/deferred/result?desired=失败
     * 设置DeferredResult对象的result属性，模拟异步操作
     * @param desired
     * @return
    */
    @GetMapping(value = "/result")
    public String settingResult(@RequestParam(value = "desired", required = false, defaultValue = "成功") String desired) {
        final ResultResponse resultResponse = new ResultResponse();
        if (ResultResponse.Msg.SUCCESS.getDesc().equals(desired)){
            resultResponse.setCode(HttpStatus.OK.value());
            resultResponse.setMsg(desired);
        }else{
            resultResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultResponse.setMsg(ResultResponse.Msg.FAILED.getDesc());
        }
        deferredResultService.settingResult(requestId,resultResponse);
        return "Done";
    }
}