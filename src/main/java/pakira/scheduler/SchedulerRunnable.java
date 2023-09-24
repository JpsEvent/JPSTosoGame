package pakira.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pakira.util.Validate;

import javax.annotation.Nonnull;

public class SchedulerRunnable extends BukkitRunnable {

    private final JavaPlugin plugin;

    private Runnable endRunnable;
    private Runnable startRunnable;
    private Runnable processRunnable;

    public SchedulerRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int getId() {
        return super.getTaskId();
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    public SchedulerRunnable whenStarted(@Nonnull Runnable runnable) {
        this.startRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    public SchedulerRunnable whenProcessed(@Nonnull Runnable runnable) {
        this.processRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    public SchedulerRunnable whenEnded(@Nonnull Runnable runnable) {
        this.endRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    public void runTimer(long delay, long period) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskTimer(this.plugin, delay, period);
    }

    public void runAsyncLater(long delay) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskLaterAsynchronously(this.plugin, delay);
    }

    public void runAsyncTimer(long delay, long period) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskTimerAsynchronously(this.plugin, delay, period);
    }

    public void runLater(long delay) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskLater(this.plugin, delay);
    }

    @Override
    public void cancel() {
        if (this.endRunnable != null)
            this.endRunnable.run();
        super.cancel();
    }

    @Override
    public void run() {
        if (this.processRunnable != null)
            this.processRunnable.run();
    }
}
