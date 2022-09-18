(ns realworld.service-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [stateful-check.core :as sc]
            [cheshire.core :as json]
            [ring.adapter.jetty :as jetty]
            [talltale.core :as tt]
            [hato.client :as hato]
            [realworld.handlers :as handlers]))

(defn rand-update-user []
  {"email"    (tt/email)
   "token"    (if (= 0 (rand-int 5)) (tt/random-password) nil)
   "username" (tt/username)
   "bio"      (if (= 0 (rand-int 5)) (tt/text) nil)
   "image"    (if (= 0 (rand-int 5)) (tt/picture-url) nil)})

(defn rand-create-user []
  {"email"    (tt/email)
   "username" (tt/username)
   "password" (tt/random-password)})

(defn rand-update-user-request []
  {"user" (rand-update-user)})

(def hc (hato/build-http-client {:connect-timeout 10000
                                 :redirect-policy :always
                                 :cookie-policy :original-server}))

(defn call-get-current-user []
  (hato/get "http://127.0.0.1:8089/user" {:as :json-string-keys
                                          :throw-exceptions? false
                                          :http-client hc}))

(defn call-create-user [user]
  (hato/post "http://127.0.0.1:8089/users"
             {:as :json-string-keys
              :http-client hc
              :throw-exceptions? false
              :body (json/encode {"user" user})
              :content-type :json}))

(defn call-login [email password]
  (hato/post "http://127.0.0.1:8089/users/login"
             {:as :json-string-keys
              :http-client hc
              :throw-exceptions? false
              :body (json/encode {"user" {"email" email "password" password}})
              :content-type :json}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; commands ;;
;;;;;;;;;;;;;;

(def get-current-user
  {:command #(call-get-current-user)
   :postcondition (fn [prev-state _ [k] val]
                    (if (:auth-token prev-state)
                      (= (:body val) (handlers/as-user-response (:auth-user prev-state)))
                      (= (:status val) 401))
                    (= (get prev-state k) val))})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; tests ;;
;;;;;;;;;;;

(def jetty-server (atom nil))

(defn- deref-server ^org.eclipse.jetty.server.Server [svr-ref]
  @svr-ref)

(defn server-fixture [f]
  (let [app (handlers/make-app (handlers/make-fake-sys))]
    (when @jetty-server
      (.stop (deref-server jetty-server)))
    (reset! jetty-server (jetty/run-jetty app {:port 8089, :join? false}))
    (f)
    (.stop (deref-server jetty-server))
    (reset! jetty-server nil)))

(use-fixtures :once server-fixture)

(def login-specification
  {:commands {:get-current-user #'get-current-user}}
  :initial-state (fn [queue] {}))

(deftest java-map-passes-sequentially
  (is (sc/specification-correct? login-specification)))

;; login scenario
;;
;; * get-current-user returns 401 because user is not logged in
;; * create-user creates new user
;; * get-current-user still returns 401
;; * login returns 200
;; * get-current-user returns user
(deftest login-test
  (let [user (rand-create-user)]
    (is (= 401 (:status (call-get-current-user))))
    (is (= 200 (:status (call-create-user user))))
    (is (= 401 (:status (call-get-current-user))))
    (is (= 200 (:status (call-login (get user "email") (get user "password"))))
        (let [cur-res (call-get-current-user)]
          (is (= 200 (:status cur-res)))
          (is (= (handlers/as-user-response user) (:body cur-res)))))))



;; update user
;;
;; * update-current-user with invalid username -> 404
;; * update-current-user with no parameters -> 400
;; * create-user to create new user
;; * update-current-user to change name
;; * get-current-user reflects name change
;; * update-current-user to change password
;; * login with updated password -> 200

