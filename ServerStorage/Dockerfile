FROM openjdk:11.0.5-jre-stretch

COPY ./build/distributions/* /dist/
WORKDIR /dist

RUN tar -xf ./ServerStorage.tar

WORKDIR /dist/ServerStorage/bin
RUN chmod +x ./ServerStorage

EXPOSE 14176
EXPOSE 14178

RUN mkdir data
WORKDIR /dist/ServerStorage/bin/data
ENTRYPOINT ["../ServerStorage"]