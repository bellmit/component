#!/bin/bash

# 递归清除不必要的文件
# https://blog.csdn.net/skylin19840101/article/details/75099240

# 删除文件
find . -name .flattened-pom.xml | xargs rm -f
find . -name .project | xargs rm -f

# 删除目录
find . -name ".settings" | xargs rm -rf
find . -name "target" | xargs rm -rf
find . -name ".classpath" | xargs rm -rf
find . -name ".factorypath" | xargs rm -rf
