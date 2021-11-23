#!/bin/bash

#-----------------------------------------------------------
# 参考自 hutool 工具
# 此脚本用于每次升级 component-parent 时替换相应位置的版本号
#
# 升级版本，包括：
# 1. 升级pom.xml中的版本号
# 2. 替换README.md和docs中的版本号
#-----------------------------------------------------------

if [ ! -n "$1" ]; then
  echo "ERROR: 新版本不存在，请指定版本号，如：upgrade_version.sh 1.0.151"
  exit
fi

pwd=$(pwd)
# 替换所有模块pom.xml中的版本
mvn versions:set -DnewVersion=$1

# 不带-SNAPSHOT的版本号，用于替换其它地方
new_version=${1%-SNAPSHOT}

# 之前的版本号
old_version=$(cat ${pwd}/bin/version.txt)
if [ ! -n "$old_version" ]; then
  echo "ERROR: 旧版本不存在，请确认bin/version.txt中信息正确"
  exit
fi

echo "$old_version 替换为新版本 $new_version"

# 替换README.md等文件中的版本
sed -i "s/${old_version}/${new_version}/g" $pwd/Readme.md

# 替换pom.xml中的版本
sed -i "s/${old_version}/${new_version}/g" $pwd/pom.xml

# 保留新版本号
echo "$new_version" >$pwd/bin/version.txt

mvn versions:commit

echo "$old_version 替换为新版本 $new_version 结束."
