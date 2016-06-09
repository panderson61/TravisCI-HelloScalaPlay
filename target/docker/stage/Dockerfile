FROM java:8
MAINTAINER JR Reyes <jayrtrooper@gmail.com>
WORKDIR /opt/docker
ADD opt /opt
RUN ["chown", "-R", "daemon:daemon", "."]
USER daemon
ENTRYPOINT ["bin/travisci-helloscalaplay"]
CMD []
