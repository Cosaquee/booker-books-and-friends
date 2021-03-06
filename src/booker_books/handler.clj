(ns booker-books.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as http]
            [clojure.string :as str]
            [clojure.core.match :refer [match]]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :as response]
            [booker-books.author-handler :as authors]
            [booker-books.friends-handler :as friends]
            [booker-books.books-handler :as books]))

(defroutes app-routes
  (GET "/author" req authors/all)
  (GET "/author/search" [request] authors/search)
  (GET "/author/:id" [request] authors/fetch)
  (POST "/author" [request] authors/create)
  (POST "/friends" [request] friends/create-friendship)
  (GET "/friends/:user_id" [request] friends/fetch-friends)
  (GET "/check-friendship/:current_user_id/:friend_id" [current_user_id friend_id] friends/check-friendship)
  (POST "/book" [request] books/create)
  (route/not-found "Not Found"))

;; TODO: Error handling
(defn check-auth [handler]
  (fn [request]
    (let [token-header (get-in request [:headers "authorization"])]
      (let [token (last (str/split token-header #" "))]
        (let [response (http/post "http://localhost:4000/auth" {:content-type :json :body (json/write-str {:token token})})]
          (match [(get (json/read-str (get response :body)) "success")]
            [true] (handler request)
            [false] (response/not-found {:status 404 :headers {} :body "unathorized"})))))))

(def app
  (-> app-routes
      (check-auth)
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-cors :access-control-allow-origin [#"http://localhost:8080"]
                 :access-control-allow-methods [:get :put :post :delete :OPTIONS])
      (wrap-defaults api-defaults)))
