@REM 使用 cmd 运行，因为在sh模式下，gpg --list-key 没有值

@REM 手动逐行运行
start cmd
cd /d D:\w\lyloou\component-parent
gpg --list-key
@REM mvn -s C:/Users/lilou/.m2/settings_ossrh.xml clean deploy -P release
