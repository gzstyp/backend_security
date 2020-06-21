package com.fwtai.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 超时耗时处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-25 10:37
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Service
public class DeferredResultService{

    private Map<String,Consumer<ResultResponse>> taskMap;

    public DeferredResultService() {
        taskMap = new ConcurrentHashMap<>();
    }

    /**
     * 将请求id与setResult映射
     *
     * @param requestId
     * @param deferredResult
     */
    public void process(String requestId, DeferredResult<ResultResponse> deferredResult) {
        // 请求超时的回调函数
        deferredResult.onTimeout(() -> {
            taskMap.remove(requestId);
            ResultResponse resultResponse = new ResultResponse();
            resultResponse.setCode(HttpStatus.REQUEST_TIMEOUT.value());
            resultResponse.setMsg(ResultResponse.Msg.TIMEOUT.getDesc());
            deferredResult.setResult(resultResponse);
        });

        Optional.ofNullable(taskMap)
            .filter(t -> !t.containsKey(requestId)).orElseThrow(() -> new IllegalArgumentException(String.format("requestId=%s is existing", requestId)));
        // 这里的Consumer，就相当于是传入的DeferredResult对象的地址
        // 所以下面settingResult方法中"taskMap.get(requestId)"就是Controller层创建的对象
        taskMap.putIfAbsent(requestId, deferredResult::setResult);
    }

    /**
     * 这里相当于异步的操作方法
     * 设置DeferredResult对象的setResult方法
     * @param requestId
     * @param resultResponse
     */
    public void settingResult(String requestId, ResultResponse resultResponse) {
        if (taskMap.containsKey(requestId)) {
            Consumer<ResultResponse> deferredResultResponseConsumer = taskMap.get(requestId);
            // 这里相当于DeferredResult对象的setResult方法
            deferredResultResponseConsumer.accept(resultResponse);
            taskMap.remove(requestId);
        }
    }
}