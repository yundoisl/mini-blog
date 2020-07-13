FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /mini-blog

COPY /target/universal/stage/bin /mini-blog/bin
COPY /target/universal/stage/lib /mini-blog/lib

RUN chmod 755 /mini-blog/bin/mini-blog
ENTRYPOINT ["/mini-blog/bin/mini-blog"]
