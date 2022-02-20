package com.example.so.grpc;

import io.grpc.stub.StreamObserver;

public class StockStaticDataRequestTestService extends StockStaticDataRequestServiceGrpc.StockStaticDataRequestServiceImplBase {

    @Override
    public void getStockStatics(StockStaticDataRequest request,
                                StreamObserver<Security> responseObserver) {
        responseObserver.onNext(Security.newBuilder()
            .setSecurity("TEST")
            .build());

        responseObserver.onCompleted();
    }

    @Override
    public void getManyStockStatics(StockStaticManyDataRequest request, StreamObserver<Security> responseObserver) {
        responseObserver.onNext(Security.newBuilder()
            .setSecurity("TEST-MANY")
            .build());
        responseObserver.onNext(Security.newBuilder()
            .setSecurity("TEST-MORE")
            .build());
        responseObserver.onCompleted();
    }
}

