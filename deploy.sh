#!/usr/bin/env bash
set -e

cd dist
git init
git add .
git commit -m "Deploy to GitHub Pages"
git push --force --quiet "git@github.com:duncanjbrown/snake-cljs.git" master:gh-pages
rm -rf .git
