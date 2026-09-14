package groot.testutil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

/**
 * Owns one JavaFX toolkit per isolated scenario and propagates application-thread failures.
 */
public final class FxTestRuntime {
    private FxTestRuntime() {
    }

    /**
     * Starts the toolkit, executes assertions on its thread, and shuts it down.
     *
     * @param action Test assertions and JavaFX operations.
     * @throws Exception If initialization, assertions, or execution fail.
     */
    public static void run(Runnable action) throws Exception {
        CountDownLatch ready = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            ready.countDown();
        });
        try {
            if (!ready.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("JavaFX startup timed out");
            }
            FutureTask<Void> task = new FutureTask<>(action, null);
            Platform.runLater(task);
            task.get(15, TimeUnit.SECONDS);
        } finally {
            Platform.exit();
        }
    }
}
