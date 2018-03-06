(ns booker-books.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as http]
            [clojure.string :as str]
            [clojure.core.match :refer [match]]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (GET "/authors" [] "Hello World")
  (route/not-found "Not Found"))

;; TODO: Error handling
(defn check-auth [handler]
  (fn [request]
    (let [token-header (get-in request [:headers "authorization"])]
      (let [token (last (str/split token-header #" "))]
        (let [response (http/post "http://localhost:4000/auth" {:content-type :json :body (json/write-str {:token token})})]
          (match [(get (json/read-str (get response :body)) "success")]
                 [true] (handler request)
                 [false] (println "Something went wrong")))
      ))))

(def app
  (-> app-routes
      (check-auth)
      (wrap-cors :access-control-allow-origin [#"http://localhost:8080"]
                 :access-control-allow-methods [:get :put :post :delete :OPTIONS])
      (wrap-defaults api-defaults)))
