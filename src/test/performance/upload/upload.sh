#!/bin/bash

_() {
  local username="default"
  local password="password"
  local test_file="testfile"
  local lua_file="upload.lua"
  local upload_url="http://localhost:8080/api/upload/test"

  echo "#### Performance test for the upload endpoint ####"

  # Erstellen der Testdatei mit einer Größe von 10 MB
  echo "Creating the test file"
  dd if=/dev/zero of=$test_file bs=1M count=10 status=none

  # Abrufen des Authentifizierungs-Cookies
  echo "Retrieving the authentication cookie"
  cookie=$(curl -s -i -X POST http://localhost:8080/j_security_check \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "j_username=$username" \
      -d "j_password=$password" | grep -i 'Set-Cookie' | awk '{print $2}' | tr -d ';')

  echo "Cookie: $cookie"

  # Schreiben des Lua-Skripts zur Verwendung mit wrk
  echo "Writing the Lua script"
  echo "
  local function read_file(path)
      local file = io.open(path, \"rb\")
      if not file then return nil end
      local content = file:read \"*a\"
      file:close()
      return content
  end

  wrk.method = \"PUT\"
  wrk.body   = read_file(\"$test_file\")
  wrk.headers[\"Cookie\"] = \"$cookie\"
  " > $lua_file

  # Ausführen des Performance-Tests mit wrk
  wrk -t10 -c100 -d10s -s $lua_file $upload_url

  rm $test_file
  rm $lua_file
}

_
