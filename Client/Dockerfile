FROM openjdk:11.0.5-jre-stretch

COPY ./build/distributions/* /dist/
WORKDIR /dist

RUN tar -xf ./Client.tar

WORKDIR /dist/Client/bin
RUN chmod +x ./Client
ENTRYPOINT ["./Client"]