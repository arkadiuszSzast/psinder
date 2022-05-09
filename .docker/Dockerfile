FROM adoptopenjdk/openjdk16
EXPOSE 8443:8443
RUN mkdir /app
RUN mkdir /keystore
COPY /application/main/build/install/main/ /app/
COPY /keystore/keystore-aws.jks /keystore/keystore-aws.jks
CMD ["./app/bin/main"]
