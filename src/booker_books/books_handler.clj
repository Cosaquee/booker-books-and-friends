(ns booker-books.books-handler
  (:require
   [clojure.data.json :as json]
   [clojurewerkz.neocons.rest :as nr]
   [clojurewerkz.neocons.rest.nodes :as nn]
   [ring.util.response :as response]
   [clojurewerkz.neocons.rest.cypher :as cy]
   [clojure.tools.logging :as log]
   [clojure.core.match :refer [match]]))

(def conn (nr/connect "http://neo4j:password@localhost:7474/db/data/"))

(defn get-response
  [graph-response]
  (let [id (get-in graph-response ["book" :metadata :id])]
    (let [data (get-in graph-response ["book" :data])]
      (merge {:id id} data))))

(def create-book-query "MERGE (book:Book {title: {title}, description: {description}, author_id: {author_id}, isbn: {isbn}}) RETURN book")

(defn create
  [request]
  (log/info "booker-books.book-handler/create")
  (let [params (get request :body)]
    (let [title (get params :title)
          description (get params :description)
          author_id (get params :author_id)
          isbn (get params :isbn)]
      (let [response (cy/tquery conn create-book-query
                                {:title title,
                                 :description description,
                                 :author_id author_id,
                                 :isbn isbn})]
        (response/response (json/write-str (get-response (first response))))))))
