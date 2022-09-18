(ns realworld.api.schema)

(declare article)
(declare comment)
(declare generic-error-model)
(declare generic-error-model-errors)
(declare login-user)
(declare login-user-request)
(declare multiple-articles-response)
(declare multiple-comments-response)
(declare new-article)
(declare new-article-request)
(declare new-comment)
(declare new-comment-request)
(declare new-user)
(declare new-user-request)
(declare profile)
(declare profile-response)
(declare single-article-response)
(declare single-comment-response)
(declare tags-response)
(declare update-article)
(declare update-article-request)
(declare update-user)
(declare update-user-request)
(declare user)
(declare user-response)

(def article
  [:map
    ["slug" string?]
    ["title" string?]
    ["description" string?]
    ["body" string?]
    ["tagList" [:sequential string?]]
    ["createdAt" string?]
    ["updatedAt" string?]
    ["favorited" boolean?]
    ["favoritesCount" int?]
    ["author" profile]
  ])

(def comment
  [:map
    ["id" int?]
    ["createdAt" string?]
    ["updatedAt" string?]
    ["body" string?]
    ["author" profile]
  ])

(def generic-error-model
  [:map
    ["errors" generic-error-model-errors]
  ])

(def generic-error-model-errors
  [:map
    ["body" [:sequential string?]]
  ])

(def login-user
  [:map
    ["email" string?]
    ["password" string?]
  ])

(def login-user-request
  [:map
    ["user" login-user]
  ])

(def multiple-articles-response
  [:map
    ["articles" [:sequential article]]
    ["articlesCount" int?]
  ])

(def multiple-comments-response
  [:map
    ["comments" [:sequential comment]]
  ])

(def new-article
  [:map
    ["title" string?]
    ["description" string?]
    ["body" string?]
    ["tagList" {:optional true} [:sequential string?]]
  ])

(def new-article-request
  [:map
    ["article" new-article]
  ])

(def new-comment
  [:map
    ["body" string?]
  ])

(def new-comment-request
  [:map
    ["comment" new-comment]
  ])

(def new-user
  [:map
    ["username" string?]
    ["email" string?]
    ["password" string?]
  ])

(def new-user-request
  [:map
    ["user" new-user]
  ])

(def profile
  [:map
    ["username" string?]
    ["bio" string?]
    ["image" string?]
    ["following" boolean?]
  ])

(def profile-response
  [:map
    ["profile" profile]
  ])

(def single-article-response
  [:map
    ["article" article]
  ])

(def single-comment-response
  [:map
    ["comment" comment]
  ])

(def tags-response
  [:map
    ["tags" [:sequential string?]]
  ])

(def update-article
  [:map
    ["title" {:optional true} string?]
    ["description" {:optional true} string?]
    ["body" {:optional true} string?]
  ])

(def update-article-request
  [:map
    ["article" update-article]
  ])

(def update-user
  [:map
    ["email" {:optional true} string?]
    ["token" {:optional true} string?]
    ["username" {:optional true} string?]
    ["bio" {:optional true} string?]
    ["image" {:optional true} string?]
  ])

(def update-user-request
  [:map
    ["user" update-user]
  ])

(def user
  [:map
    ["email" string?]
    ["token" string?]
    ["username" string?]
    ["bio" string?]
    ["image" string?]
  ])

(def user-response
  [:map
    ["user" user]
  ])

