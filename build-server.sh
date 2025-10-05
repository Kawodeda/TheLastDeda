#!/bin/bash

OUT_DIR="dist/server"

echo "Start server-pack build..."

mkdir -p "$RESULT_DIR"

rsync -av --ignore-existing "shared" "$RESULT_DIR/"
rsync -av --ignore-existing "server" "$RESULT_DIR/"

echo "Done."
