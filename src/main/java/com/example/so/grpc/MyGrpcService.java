package com.example.so.grpc;

import io.grpc.examples.routeguide.Feature;
import io.grpc.examples.routeguide.Point;
import io.grpc.examples.routeguide.RouteGuideGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MyGrpcService extends RouteGuideGrpc.RouteGuideImplBase {

    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
        responseObserver.onNext(Feature.newBuilder()
                .setLocation(request)
                .setName("Name")
            .build());

        responseObserver.onCompleted();
    }

}
