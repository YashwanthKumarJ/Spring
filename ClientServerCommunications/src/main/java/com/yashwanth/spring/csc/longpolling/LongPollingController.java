package com.yashwanth.spring.csc.longpolling;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/long-polling")
public class LongPollingController {

    @RequestMapping("/v1")
    @ResponseBody
    public DeferredResult<String> getResult() {

        DeferredResult<String> deferredResult = new DeferredResult<>();

        /* runAsync is used. This will use thread in the common pool to run the task. We could also create a thread-pool
         here to execute the tasks (to get better control on the threads which are part of service) */
        CompletableFuture.runAsync(() -> {
            try {
                // Adding a delay to mimic a real use case where data for the response received after a while.
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            deferredResult.setResult("Long Polling : Response sent after 10 secs from the Deferred Result.");
        });

        return deferredResult;
    }
}
