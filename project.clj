(defproject booker-books "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]
            [cider/cider-nrepl "0.17.0-SNAPSHOT"]
            [refactor-nrepl "2.4.0-SNAPSHOT"]]
  :ring {:handler booker-books.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [clj-http "3.7.0"]
                        [ring-cors "0.1.11"]
                        [org.clojure/core.match "0.3.0-alpha5"]
                        [org.clojure/data.json "0.2.6"]
                        [ring/ring-mock "0.3.0"]]}})

