build: dist/ dist/style.css dist/snake.js dist/index.html

clean:
	rm -rf dist

dist/:
	mkdir dist

dist/index.html:
	cp resources/prod-index.html $@

dist/style.css:
	cp resources/public/css/style.css $@

dist/snake.js:
	clojure -m figwheel.main -co prod.cljs.edn -c
