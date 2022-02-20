package com.example.so.grpc;

import com.asarkar.grpc.test.GrpcCleanupExtension;
import com.asarkar.grpc.test.Resources;
import com.google.common.collect.Lists;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
@SpringBootTest
@ExtendWith(GrpcCleanupExtension.class)
@DirtiesContext
class TestGrpc {

    @GrpcClient("stocks")
    private StockStaticDataRequestServiceGrpc.StockStaticDataRequestServiceBlockingStub stub;

    private static Server server;

    private static Resources resources;

    @BeforeAll
    public static void setUp() throws Exception {
        server = ServerBuilder
            .forPort(9091)
            .addService(new StockStaticDataRequestTestService())
            .build();
        server.start();

        // Prevent the JVM from shutting down while the server is running
        final Thread awaitThread = new Thread(() -> {
            try {
                log.info("Waiting for requests...");
                server.awaitTermination();
                log.info("Server shut down");
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        awaitThread.setName("grpc-server-container");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Test
    @DirtiesContext
    void testClient() {
        resources.register(server, Duration.ofSeconds(10));
        StockStaticManyDataRequest request = StockStaticManyDataRequest.newBuilder()
            .addAllTickerSymbols(Lists.newArrayList("AAPL"))
            .build();

        AtomicInteger messagesCounted = new AtomicInteger(0);

        stub.getManyStockStatics(request).forEachRemaining(security -> {
            messagesCounted.incrementAndGet();
        });

        assertThat(messagesCounted.get(), is(2));
    }

    @Test
    @DirtiesContext
    void testClientAgain() {
        resources.register(server, Duration.ofSeconds(10));

        StockStaticManyDataRequest request = StockStaticManyDataRequest.newBuilder()
                .addAllTickerSymbols(Lists.newArrayList("AAPL"))
                .build();

        AtomicInteger messagesCounted = new AtomicInteger(0);

        stub.getManyStockStatics(request).forEachRemaining(security -> {
            messagesCounted.incrementAndGet();
        });

        assertThat(messagesCounted.get(), is(2));
    }
}
