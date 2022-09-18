.PHONY: apigen
apigen:
	java -jar ~/src/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar generate -i openapi/realworld-openapi.yml -g clojure-server -o ./src/main/clojure/realworld/api -p baseNamespace=realworld.api

.PHONY: test
test:
	./bin/kaocha

.PHONY: cover
cover:
	./bin/kaocha --plugin cloverage

.PHONY: watch
watch:
	./bin/kaocha --watch

.PHONY: lint
lint:
	clj -M:test:eastwood

.PHONY: uberjar
uberjar:
	clj -T:build uber

.PHONY: run
run: uberjar
	java -cp target/realworld.main-1.2.null-standalone.jar realworld.main