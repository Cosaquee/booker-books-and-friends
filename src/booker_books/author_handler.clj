(ns booker-books.author-handler
  (:require
            [clojure.data.json :as json]
            [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [ring.util.response :as response]
            [clojurewerkz.neocons.rest.cypher :as cy]))

(def conn (nr/connect "http://neo4j:password@localhost:7474/db/data/"))

;;Queries
(def fetch-author-query "MATCH (author {type: 'author'}) return author")

(defn create
  [request]
  (let [params (get request :body)]
    (nn/create conn (assoc params :type "author"))
    (response/response "Created new author")))

(defn get-author
  [response]
  (get-in response ["author" :data]))

(defn all
  [req]
  (let [authors (cy/tquery conn fetch-author-query)]
    (response/response (json/write-str (map get-author authors)))))
