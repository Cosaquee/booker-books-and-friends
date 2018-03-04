(ns booker-books.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(defn check-auth [handler]
  (fn [request]
    (println "It works")
    (handler request)))

(def app
  (-> app-routes
    (wrap-defaults site-defaults)
    (check-auth)))
