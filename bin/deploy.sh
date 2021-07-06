#!/bin/bash
# 使用 cmd 运行，因为在sh模式下，gpg --list-key 没有值
mvn -s C:/Users/lilou/.m2/settings_ossrh.xml clean deploy -P release
