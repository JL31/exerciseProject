#!/bin/bash

# ======================================================================================================================
#
# This file will concatenate SQL files to create an init.sql file used along with the docker-compose.yml file to
# initialize the PostgreSQL database in the container
#
# ======================================================================================================================

output_file="database_initialization.sql"

rm -f "${output_file}"

for file in *.sql
do
    if [[ -f "$file" ]]; then
        cat "${file}" >> "${output_file}"
    fi
done
