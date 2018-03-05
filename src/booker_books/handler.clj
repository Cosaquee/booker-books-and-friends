(ns booker-books.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as http]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(defn check-auth [handler]
  (fn [request]
    (println "It works")
    (println request)
    (handler request)))

(def app
  (-> app-routes
    (wrap-defaults api-defaults)
    (check-auth)
    (wrap-cors :access-control-allow-origin [#"http://localhost:8080"]
               :access-control-allow-methods [:get :put :post :delete :options]))
  )
