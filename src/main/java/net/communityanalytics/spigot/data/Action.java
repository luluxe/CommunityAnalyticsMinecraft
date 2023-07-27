package net.communityanalytics.spigot.data;

import java.time.LocalDateTime;

public class Action {
    private final String name;
    private final LocalDateTime date;

    public Action(String name) {
        this.name = name;
        this.date = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
