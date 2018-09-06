FROM clojure

# WORKDIR /tmp
# RUN apt-get update && apt-get install -y git emacs
# RUN git clone https://github.com/thi-ng/geom.git 
# RUN cd geom && ./tangle-all.sh && ./install-all.sh
# RUN git clone https://github.com/thi-ng/math.git
# RUN cd math && ./tangle.sh src/*.org test/*.org
# RUN cd math/babel && lein do clean, install
# RUN git clone https://github.com/thi-ng/color.git
# RUN cd color && ./tangle.sh src/*.org test/*.org
# RUN cd color/babel && lein do clean, install

WORKDIR /usr/src/app

ENTRYPOINT [ "lein" ]
CMD ["do deps, repl"]