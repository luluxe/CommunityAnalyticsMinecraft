package net.communityanalytics.common.interfaces;

public interface ILogger {
    void printDebug(String message);

    void printInfo(String message);

    void printError(String message);

    boolean isPluginEnable();
}
