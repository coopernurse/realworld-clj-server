(ns realworld.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.connection :as connection]))

(def jdbcUrl (or (System/getenv "JDBC_URL") "jdbc:h2:mem:test_mem"))
(def dbType (or (System/getenv "JDBC_TYPE") "h2"))

(defn make-ds [dbType jdbcUrl]
  (connection/->pool com.zaxxer.hikari.HikariDataSource
                     {:dbtype dbType :jdbcUrl jdbcUrl}))

(def migrations
  ["create table users (
    username    varchar(64) primary key,
    email       varchar(128),
    token       varchar(255),
    bio         varchar(4096),
    image       varchar(255),
    created_at  bigint,
    updated_at  bigint
  )"])

(defn create-migration-table [ds]
  (jdbc/execute! ds ["create table if not exists migrations (sql varchar(4096) not null)"]))

(defn get-migrations-applied [ds]
  (let [d (jdbc/with-options ds {:builder-fn rs/as-unqualified-lower-maps})]
    (set (map :sql (jdbc/execute! d ["select sql from migrations"])))))

(defn apply-migration [ds q]
  (jdbc/execute! ds [q])
  (jdbc/execute! ds ["insert into migrations (sql) values (?)" q]))

(defn apply-migrations [ds]
  (create-migration-table ds)
  (let [ma (get-migrations-applied ds)
        unapplied (filter #(not (contains? ma %1)) migrations)]
    (map #(apply-migration ds %1) unapplied)))





;;        ["email" string?]
;; ["token" string?]
;; ["username" string?]
;; ["bio" string?]
;; ["image" string?]