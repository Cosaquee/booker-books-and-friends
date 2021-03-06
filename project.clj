(defproject booker-books "0.1.0-SNAPSHOT"
  :description "Booker Friends and Books management service"
  :url "https://github.com/Cosaquee/booker-books-and-friends"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]
            [cider/cider-nrepl "0.17.0-SNAPSHOT"]
            [refactor-nrepl "2.4.0-SNAPSHOT"]
            [lein-cljfmt "0.6.0"]]
  :ring {:handler booker-books.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [clj-http "3.7.0"]
                        [ring-cors "0.1.11"]
                        [ring/ring-json "0.4.0"]
                        [org.clojure/core.match "0.3.0-alpha5"]
                        [clojurewerkz/neocons "3.2.0"]
                        [org.clojure/data.json "0.2.6"]
                        [ring/ring-mock "0.3.0"]
                        [org.clojure/tools.logging "0.4.0"]]}})
