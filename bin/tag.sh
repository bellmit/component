#!/usr/bin/env sh

# 获取tag名称：dev_2110221510
time=$(date "+%y%m%d%H%M")
t_tag="dev_${time}"

if [[ -n "$1" ]];
then
  t_tag="$1"
fi

# 复制tag到系统剪切板
echo ${t_tag} | clip.exe
echo "TAG: ${t_tag} copied."

# 打标签
git tag ${t_tag}
git push origin ${t_tag}

# 打开Github网页
start https://github.com/lyloou/component
