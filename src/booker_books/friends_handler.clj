(ns	booker-books.friends-handler
  (:require
   [clojure.data.json :as json]
   [clojurewerkz.neocons.rest :as nr]
   [clojurewerkz.neocons.rest.nodes :as nn]
   [ring.util.response :as response]
   [clojurewerkz.neocons.rest.cypher :as cy]
   [clojure.tools.logging :as log]
   [clojure.core.match :refer [match]]))

(def conn (nr/connect "http://neo4j:password@localhost:7474/db/data/"))

(def create-friendship-query "MATCH (current_user:User {id: {current_user_id}}), (friend:User {id: {friend_id}}) MERGE (current_user)-[:IS_FRIEND]-(friend)")

(def merge-user "MERGE (current_user:User {id: {id}}) MERGE (friend_user:User {id: {friend_id}})")

(def merge-user-query "MERGE (user:User {id: {id}})")

(def check-friendship-query "MATCH (a:User {id: {current_user_id}}), (friend:User {id: {friend_id}}) RETURN EXISTS ((a)-[:IS_FRIEND]-(friend))")

(defn check-friendship
  [request]
  (let [current_user_id (Integer/parseInt (get-in request [:route-params :current_user_id]))
        friend_id (Integer/parseInt (get-in request [:route-params :friend_id]))]
    (let [query-result (cy/query conn check-friendship-query {:current_user_id current_user_id :friend_id friend_id})]
      (match [(:data query-result)]
        [[[true]]] (response/response (json/write-str {:friends true}))
        [[[false]]] (response/response (json/write-str {:friends false}))
        [[]] (response/response (json/write-str {:friends false}))))))

(defn ensure-users [ids]
  (doseq [id ids]
    (cy/tquery conn merge-user-query {:id id})))

(defn create-friendship
  [request]
  (let [params (get request :body)]
    (let [user_id (get params :current_user_id)
          friend_id (Integer/parseInt (get params :friend_id))]
      (ensure-users [user_id friend_id])
      (cy/tquery conn create-friendship-query {:current_user_id user_id, :friend_id friend_id}))))

