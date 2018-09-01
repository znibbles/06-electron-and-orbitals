FROM clojure

WORKDIR /tmp
RUN apt-get update && apt-get install -y git emacs
RUN git clone https://github.com/thi-ng/geom.git 
RUN cd geom && ./tangle-all.sh && ./install-all.sh

WORKDIR /usr/src/app

ENTRYPOINT [ "lein" ]
CMD ["repl"]