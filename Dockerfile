FROM oraclelinux:8-slim

# Install required packages
RUN microdnf install -y tar gzip && \
    microdnf clean all

ENV JAVA_HOME=/usr/java/jdk-21.0.2
ENV PATH=$JAVA_HOME/bin:$PATH
RUN curl -fSL https://download.oracle.com/java/21/archive/jdk-21.0.2_linux-x64_bin.tar.gz -o /tmp/jdk.tar.gz && \
    mkdir -p /usr/java && \
    tar -xvzf /tmp/jdk.tar.gz -C /usr/java && \
    rm /tmp/jdk.tar.gz

RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app
RUN chown -R appuser:appuser /app

USER appuser

COPY --chown=appuser:appuser build/libs/*.jar /app/app.jar

EXPOSE 443

CMD ["java", "-jar", "/app/app.jar"]
