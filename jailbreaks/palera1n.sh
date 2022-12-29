#!/bin/bash

folder_name="palera1n"

if [ -d "./$folder_name" ]; then
  rm -rf $folder_name
fi

git clone --recursive https://github.com/palera1n/palera1n $folder_name