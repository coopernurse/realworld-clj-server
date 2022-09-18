(ns realworld.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [realworld.handlers :as handlers]))

(defn -main
  "Starts the realworld clojure server"
  []
  (let [app (handlers/make-app (handlers/make-fake-sys))]
    (jetty/run-jetty app {:port 8089, :join? true})))