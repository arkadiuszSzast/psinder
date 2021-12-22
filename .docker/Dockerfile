FROM adoptopenjdk/openjdk16
EXPOSE 8080:8080
EXPOSE 8443:8443
RUN mkdir /app
RUN mkdir /keystore
COPY /build/install/psinder/ /app/
COPY /keystore/keystore-aws.jks /keystore/keystore-aws.jks
CMD ["./app/bin/psinder"]
