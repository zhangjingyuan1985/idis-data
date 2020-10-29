FROM livingobjects/jre8:latest

ENV IDIS_DATA_HOME="/home/idis/idis-data"
ENV PATH="${IDIS_DATA_HOME}/bin:${PATH}"

COPY bin  ${IDIS_DATA_HOME}/bin
COPY lib  ${IDIS_DATA_HOME}/lib

EXPOSE 8080/tcp

WORKDIR ${IDIS_DATA_HOME}

ENTRYPOINT ["idis-data"]