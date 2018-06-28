(ns booker-books.author-handler
  (:require
   [clojure.tools.logging :as log]
   [clojure.data.json :as json]
   [clojurewerkz.neocons.rest :as nr]
   [clojurewerkz.neocons.rest.nodes :as nn]
   [ring.util.response :as response]
   [clojurewerkz.neocons.rest.cypher :as cy]))

(def conn (nr/connect "http://neo4j:password@localhost:7474/db/data/"))

;;Queries
(def fetch-author-query "MATCH (author:Author) WHERE id(author) = {id} RETURN author")

(def create-author "MERGE (author:Author {name: {name}, surname: {surname}, avatar_url: {avatar_url}, description: {description}}) RETURN author")

(def search-query "MATCH (author:Author) WHERE author.name CONTAINS {name} OR author.surname CONTAINS {surname} RETURN author")

(defn get-response
  [graph-response]
  (let [id (get-in graph-response ["author" :metadata :id])]
    (let [data (get-in graph-response ["author" :data])]
      (merge {:id id} data))))

(defn fetch
  [request]
  (log/info "booker-books.author-handler/fetch")
  (let [id (get-in request [:route-params :id])]
    (log/debug "Incoming author ID is ", id)
    (let [query-response (cy/tquery conn fetch-author-query {:id (Integer/parseInt id)})]
      (log/debug "Query response of fetching author is" query-response)
      (response/response (json/write-str (get-response (first query-response)))))))

(defn search
  [request]
  (log/info "booker-books.author-handler/search")
  (let [query (get request :query-string)]
    (let [query-response (cy/tquery conn search-query {:name query :surname query})]
      (response/response (json/write-str (map get-response query-response))))))

(defn create
  [request]
  (let [params (get request :body)]
    (let [name (get params :name)
          surname (get params :surname)
          description (get params :description)
          birthDate (get params :birthDate)]
      (let [response  (cy/tquery conn create-author {:name name,
                                                     :surname surname,
                                                     :avatar_url (str "https://api.adorable.io/avatars/285/" (rand-int 300))
                                                     :description description})]
        (response/response (json/write-str (get-response (first response))))))))

(defn get-author
  [response]
  (get-in response ["author" :data]))

(defn all
  [req]
  (let [authors (cy/tquery conn fetch-author-query)]
    (response/response (json/write-str (map get-author authors)))))

