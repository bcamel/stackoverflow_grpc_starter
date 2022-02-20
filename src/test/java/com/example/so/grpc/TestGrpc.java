package com.example.so.grpc;

import com.google.common.collect.Lists;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
@SpringBootTest
class TestGrpc {

    @GrpcClient("stocks")
    private StockStaticDataRequestServiceGrpc.StockStaticDataRequestServiceBlockingStub stub;

    @BeforeAll
    public static void setUp() throws Exception {
        final Server localServer = ServerBuilder
            .forPort(9091)
            .addService(new StockStaticDataRequestTestService())
            .build();
        localServer.start();

        // Prevent the JVM from shutting down while the server is running
        final Thread awaitThread = new Thread(() -> {
            try {
                log.info("Waiting for requests...");
                localServer.awaitTermination();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        awaitThread.setName("grpc-server-container");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Test
    void testClient() {
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
    void testClientAgain() {
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
