package net.communityanalytics.common;

import io.sentry.Sentry;

public class SentryManager {
    /**
     * Init : Configure Sentry as soon as possible in your application's lifecycle:
     */
    public static void init() {
        Sentry.init(options -> {
            options.setDsn("https://ce08dab6ccfb4079a363bd4c5e2b8c3d@o467294.ingest.sentry.io/4505081944539136");
            // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
            // We recommend adjusting this value in production.
            // options.setTracesSampleRate(1.0);
            // When first trying Sentry it's good to see what the SDK is doing:
            options.setDebug(false);
        });
    }

    /**
     * @param exception Exception
     */
    public static void capture(Exception exception) {
        Sentry.captureException(exception);
    }
}
