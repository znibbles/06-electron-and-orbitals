(defproject my-thing_project "0.1.0-SNAPSHOT"
  :description  "thi.ng workshop WS-LDN-10"
  :url          "http://workshop.thi.ng"
  :license      {:name "Apache Software License"
                 :url "http://www.apache.org/licenses/"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [thi.ng/geom "1.0.0-RC3"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"])