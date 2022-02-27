package com.lyloou.component.tool.execcommand;

import java.util.Map;
import java.util.TreeMap;

public enum ShellEnum {
    WEB_UPGRADE("更新web",
            "cd ~/w/box/compose/data/box-web;" +
                    "rm -rf box;cp -r ~/w/box/box-web box;" +
                    "cd ~/w/box/compose;" +
                    "git pull;" +
                    "docker-compose restart common-nginx"),
    PING("ping", "echo ip138.com")
    ;
    private final String title;
    private final String shellContent;

    ShellEnum(String title, String shellContent) {
        this.title = title;
        this.shellContent = shellContent;
    }

    public String getName() {
        return name();
    }

    public String getTitle() {
        return title;
    }

    public String getShellContent() {
        return shellContent;
    }

    public static ShellEnum getShell(String name) {
        ShellEnum[] shellEnums = ShellEnum.values();
        for (ShellEnum shellEnum : shellEnums) {
            if (shellEnum.name().equals(name)) {
                return shellEnum;
            }
        }
        throw new IllegalArgumentException("无效的名称：" + name);
    }

    public static Map<String, String> features() {
        ShellEnum[] shellEnums = ShellEnum.values();
        Map<String, String> map = new TreeMap<>();
        for (ShellEnum shellEnum : shellEnums) {
            map.put(shellEnum.getName(), shellEnum.getTitle());
        }
        return map;
    }
}
