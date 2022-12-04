package io.lhysin.reactive;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import io.lhysin.reactive.dto.CreatePointReq;
import io.lhysin.reactive.dto.PointRes;
import io.lhysin.reactive.type.PointCreatedType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@SpringBootTest(classes = SpringReactiveApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ActiveProfiles("test")
class PointWebTests {

    @LocalServerPort
    private int port;

    // helper methods to create default instances
    private WebClient createDefaultClient() {
        HttpClient httpClient = HttpClient
            .create()
            .wiretap(true);
        return WebClient.builder()
            .baseUrl("http://localhost:" + port)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Test
    public void webClientTest() {
        WebClient client = createDefaultClient();

        Flux<PointRes> pointResFlux = client.get().uri("/points/testUserID")
            .retrieve()
            .bodyToFlux(PointRes.class);

        List<PointRes> pointResList = pointResFlux.collectList().block();
        log.debug("pointResList : {}", pointResList);
    }

    @Test
    public void createTest() {
        WebClient client = createDefaultClient();
        String userId = "createTest";

        CreatePointReq req = CreatePointReq.builder()
            .userId(userId)
            .amount(new BigDecimal(300))
            .pointCreatedType(PointCreatedType.EVENT)
            .createdBy("MY")
            .build();

        Mono<BigDecimal> availablePointMono = client.get().uri("/points/" + userId + "/summary")
            .retrieve()
            .bodyToMono(BigDecimal.class);

        Mono<Void> createMono = client.post().uri("/points/" + userId)
            .body(Mono.just(req), CreatePointReq.class)
            .retrieve()
            .bodyToMono(Void.class);

        Flux<Void> create20Req = Flux.range(0, 20)
            .flatMap(integer -> createMono);

        log.debug("available Point!!!! : {}", availablePointMono.block());
        create20Req.collectList().block();
        log.debug("available Point!!!! : {}", availablePointMono.block());

    }
}
