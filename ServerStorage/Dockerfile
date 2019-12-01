FROM openjdk:11.0.5-jre-stretch

COPY ./build/distributions/* /dist/
WORKDIR /dist

RUN tar -xf ./ServerStorage.tar

WORKDIR /dist/ServerStorage/bin
RUN ls
RUN chmod +x ./ServerStorage
EXPOSE 14176
CMD ["./ServerStorage"]