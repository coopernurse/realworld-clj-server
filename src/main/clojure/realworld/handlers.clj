(ns realworld.handlers
  (:require [realworld.api.schema :as sc]
            [malli.core :as m]
            [malli.error :as me]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [reitit.ring :as ring]))

(defn make-fake-sys []
  (let [users-by-email (atom {})]
    {:db-user-put #(swap! users-by-email assoc (get %1 "email") %1)
     :db-user-get-by-email #(get @users-by-email %1)}))

(defn as-json [status body]
  {:status status
   :body (json/encode body)
   :headers {"Content-Type" "application/json"}})

(defn as-user-response [user]
  {"user" (select-keys user ["email" "token" "username" "bio" "image"])})

;; Create an article
;; Parameters:
;;  body:  article :new-article-request - Article to create
;; Returns: :single-article-response    
;; :create-article not-implemented
(defn create-article [sys req]
  ;; to get json body:
  ;;(println (:json-params (:request ctx)))
  {:status 200 :body "foo"})

(defn ok-handler [sys req]
  (as-json 200 {:status "ok"}))

;; Register a new user
;; Parameters: 
;;  body:  body :new-user-request - Details of the new user to register 
;; Returns: :user-response
(defn create-user [sys req]
  (let [user (get-in req [:body "user"])]
    ((:db-user-put sys) user)
    (as-json 200 (select-keys user ["email" "username"]))))

;; Get current user
;; Parameters: This endpoint has no parameters
;; Returns: :user-response
(defn get-current-user [sys req]
  (let [email (get-in req [:session :email])
        user  (if email ((:db-user-get-by-email sys) email) nil)]
    (if user
      (as-json 200 (as-user-response user))
      (as-json 401 {:error "not logged in"}))))

;; Existing user login
;; Parameters: 
;;  body:  body :login-user-request - Credentials to use 
;; Returns: :user-response
(defn login [sys req]
  (let [email    (get-in req [:body "user" "email"])
        pass     (get-in req [:body "user" "password"])
        user     (if email ((:db-user-get-by-email sys) email) nil)
        new-sess (assoc (:session req) :email email)]
    (if (and user (= pass (get user "password")))
      (-> (as-json 200 (as-user-response user))
          (assoc :session new-sess))
      (as-json 400 {:error "invalid email and password"}))))

;; Update current user
;; Parameters: 
;;  body:  body :update-user-request - User details to update. 
;;         At least **one** field is required. 
;; Returns: :user-response
(defn update-current-user [sys req]
  (as-json 200 {:foo "bar"}))

(defn wrap-input-schema [handler schema]
  (if (m/schema? schema) ;; do we have a schema? not all requests expect a JSON object, so this can be nil
    (fn [req]
      (let [body (:body req)
            err  (me/humanize (m/explain schema body))]
        (if err
          (as-json 400 {:error err})
          (handler req))))
    ;; else: no schema, return handler
    handler))

(defn wrap-handler [h sys input-schema]
  (-> (partial h sys) ;; pass sys into handle via partial application
      (wrap-input-schema input-schema)
      (wrap-session {:store (cookie-store {:key "0123450123450123"})})
      (wrap-json-body)))

(defn make-app [sys]
  (ring/ring-handler
   (ring/router
    [["/ok"          {:get  (wrap-handler ok-handler sys nil)}]
     ["/user"        {:get  (wrap-handler get-current-user sys nil)}]
     ["/users"       {:post (wrap-handler create-user sys (m/schema sc/new-user-request))}]
     ["/users/login" {:post (wrap-handler login sys (m/schema sc/login-user-request))}]])
   (constantly {:status 404, :body "Not Found"})))
