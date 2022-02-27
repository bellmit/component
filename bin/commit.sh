#!/usr/bin/env bash
cd $(dirname $0)

# add commit msg, default msg is ":sparkles: auto commit"
msg=":sparkles: auto commit"
if [[ -n "$1" ]];
then
  msg="$1"
fi

cd ..
pwd
git pull
git add .
git commit -m "$msg"
git push origin master