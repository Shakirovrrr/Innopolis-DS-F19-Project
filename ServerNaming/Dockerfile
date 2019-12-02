FROM openjdk:11.0.5-jre-stretch

COPY ./build/distributions/* /dist/
WORKDIR /dist

RUN tar -xf ./ServerNaming.tar

WORKDIR /dist/ServerNaming/bin
RUN chmod +x ./ServerNaming

EXPOSE 14175
EXPOSE 14177

ENTRYPOINT ["./ServerNaming"]