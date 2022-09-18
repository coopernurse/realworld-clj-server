(ns realworld.api.routes)

(defn not-implemented [body]
  {:status 501 :body "Not Implemented"})

;; stubbed handlers for this API
(def handlers
  {

  ;;
  ;; ArticlesApi
  ;;

    ;; Create an article
    ;; Parameters: 
    ;;  body:  article :new-article-request - Article to create 
    ;; Returns: :single-article-response
    :create-article not-implemented

    ;; Delete an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article to delete  [default to null]
    ;; Returns: null (empty response body)
    :delete-article not-implemented

    ;; Get an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article to get  [default to null]
    ;; Returns: :single-article-response
    :get-article not-implemented

    ;; Get recent articles globally
    ;; Parameters: 
    ;;  query-string:  tag string? - Filter by tag  [optional] [default to null]
    ;;  query-string:  author string? - Filter by author (username)  [optional] [default to null]
    ;;  query-string:  favorited string? - Filter by favorites of a user (username)  [optional] [default to null]
    ;;  query-string:  limit int? - Limit number of articles returned (default is 20)  [optional] [default to 20]
    ;;  query-string:  offset int? - Offset/skip number of articles (default is 0)  [optional] [default to 0]
    ;; Returns: :multiple-articles-response
    :get-articles not-implemented

    ;; Get recent articles from users you follow
    ;; Parameters: 
    ;;  query-string:  limit int? - Limit number of articles returned (default is 20)  [optional] [default to 20]
    ;;  query-string:  offset int? - Offset/skip number of articles (default is 0)  [optional] [default to 0]
    ;; Returns: :multiple-articles-response
    :get-articles-feed not-implemented

    ;; Update an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article to update  [default to null]
    ;;  body:  article :update-article-request - Article to update 
    ;; Returns: :single-article-response
    :update-article not-implemented

  ;;
  ;; CommentsApi
  ;;

    ;; Create a comment for an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article that you want to create a comment for  [default to null]
    ;;  body:  comment :new-comment-request - Comment you want to create 
    ;; Returns: :single-comment-response
    :create-article-comment not-implemented

    ;; Delete a comment for an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article that you want to delete a comment for  [default to null]
    ;;  path-param:  id int? - ID of the comment you want to delete  [default to null]
    ;; Returns: null (empty response body)
    :delete-article-comment not-implemented

    ;; Get comments for an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article that you want to get comments for  [default to null]
    ;; Returns: :multiple-comments-response
    :get-article-comments not-implemented

  ;;
  ;; DefaultApi
  ;;

    ;; Get tags
    ;; Parameters: This endpoint has no parameters
    ;; Returns: :tags-response
    :tags-get not-implemented

  ;;
  ;; FavoritesApi
  ;;

    ;; Favorite an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article that you want to favorite  [default to null]
    ;; Returns: :single-article-response
    :create-article-favorite not-implemented

    ;; Unfavorite an article
    ;; Parameters: 
    ;;  path-param:  slug string? - Slug of the article that you want to unfavorite  [default to null]
    ;; Returns: :single-article-response
    :delete-article-favorite not-implemented

  ;;
  ;; ProfileApi
  ;;

    ;; Follow a user
    ;; Parameters: 
    ;;  path-param:  username string? - Username of the profile you want to follow  [default to null]
    ;; Returns: :profile-response
    :follow-user-by-username not-implemented

    ;; Get a profile
    ;; Parameters: 
    ;;  path-param:  username string? - Username of the profile to get  [default to null]
    ;; Returns: :profile-response
    :get-profile-by-username not-implemented

    ;; Unfollow a user
    ;; Parameters: 
    ;;  path-param:  username string? - Username of the profile you want to unfollow  [default to null]
    ;; Returns: :profile-response
    :unfollow-user-by-username not-implemented

  ;;
  ;; UserAndAuthenticationApi
  ;;

    ;; Register a new user
    ;; Parameters: 
    ;;  body:  body :new-user-request - Details of the new user to register 
    ;; Returns: :user-response
    :create-user not-implemented

    ;; Get current user
    ;; Parameters: This endpoint has no parameters
    ;; Returns: :user-response
    :get-current-user not-implemented

    ;; Existing user login
    ;; Parameters: 
    ;;  body:  body :login-user-request - Credentials to use 
    ;; Returns: :user-response
    :login not-implemented

    ;; Update current user
    ;; Parameters: 
    ;;  body:  body :update-user-request - User details to update. At least **one** field is required. 
    ;; Returns: :user-response
    :update-current-user not-implemented

  })

;; (defn make-routes [handler]
;;   (route/expand-routes 
;;   #{

;;   ["/articles" :post (:create-article handler) :route-name :create-article]
;;   ["/articles/:slug" :delete (:delete-article handler) :route-name :delete-article]
;;   ["/articles/:slug" :get (:get-article handler) :route-name :get-article]
;;   ["/articles" :get (:get-articles handler) :route-name :get-articles]
;;   ["/articles/feed" :get (:get-articles-feed handler) :route-name :get-articles-feed]
;;   ["/articles/:slug" :put (:update-article handler) :route-name :update-article]

;;   ["/articles/:slug/comments" :post (:create-article-comment handler) :route-name :create-article-comment]
;;   ["/articles/:slug/comments/:id" :delete (:delete-article-comment handler) :route-name :delete-article-comment]
;;   ["/articles/:slug/comments" :get (:get-article-comments handler) :route-name :get-article-comments]

;;   ["/tags" :get (:tags-get handler) :route-name :tags-get]

;;   ["/articles/:slug/favorite" :post (:create-article-favorite handler) :route-name :create-article-favorite]
;;   ["/articles/:slug/favorite" :delete (:delete-article-favorite handler) :route-name :delete-article-favorite]

;;   ["/profiles/:username/follow" :post (:follow-user-by-username handler) :route-name :follow-user-by-username]
;;   ["/profiles/:username" :get (:get-profile-by-username handler) :route-name :get-profile-by-username]
;;   ["/profiles/:username/follow" :delete (:unfollow-user-by-username handler) :route-name :unfollow-user-by-username]

;;   ["/users" :post (:create-user handler) :route-name :create-user]
;;   ["/user" :get (:get-current-user handler) :route-name :get-current-user]
;;   ["/users/login" :post (:login handler) :route-name :login]
;;   ["/user" :put (:update-current-user handler) :route-name :update-current-user]

;;   }))

;; (defn create-server [handler & {:keys [port] :or {port 8089}}]
;;   (http/create-server
;;    {:io.pedestal.http/router :prefix-tree
;;     ::http/routes (make-routes handler)
;;     ::http/type   :jetty
;;     ::http/port   port}))

;; (defn start [handler]
;;   (http/start (create-server handler)))