package com.yashwanth.spring.csc.sse;


import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RestController()
@RequestMapping("/server-events")
public class ServerSentEventsController {

    /* Using Flux<String> as return value
     */
    @GetMapping(path = "/v1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> handleV1() throws IOException {
        Stream<String> files = Files.lines(Path.of("./pom.xml"));
        return Flux.fromStream(files)
                .filter(s -> !s.isBlank())
                .delayElements(Duration.ofMillis(500));
    }

    /* Using Flux<ServerSentEvent<String>> as the return value
     */
    @GetMapping(path = "/v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> handleV2() throws IOException {

        AtomicInteger counter = new AtomicInteger(1);

        Stream<String> files = Files.lines(Path.of("./pom.xml"));
        return Flux.fromStream(files)
                .filter(s -> !s.isBlank())
                .map(s -> ServerSentEvent.<String>builder().event("YJEvent").id(String.valueOf(counter.getAndIncrement())).data(s).build())
                .delayElements(Duration.ofMillis(500));
    }
}
