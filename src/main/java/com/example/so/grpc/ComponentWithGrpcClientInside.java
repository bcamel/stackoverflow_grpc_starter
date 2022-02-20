package com.example.so.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class ComponentWithGrpcClientInside {

    @GrpcClient("stocks")
    private StockStaticDataRequestServiceGrpc.StockStaticDataRequestServiceBlockingStub stub;

}
