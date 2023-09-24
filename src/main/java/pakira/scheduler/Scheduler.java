package pakira.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import pakira.util.Validate;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Scheduler {

    private final JavaPlugin plugin;
    private final List<Function<SchedulerRunnable, Boolean>> freezeFilters;
    private final List<Function<SchedulerRunnable, Boolean>> terminateFilters;

    private Runnable endRunnable;
    private Runnable startRunnable;
    private SchedulerRunnable task;

    private boolean async;

    private long after;
    private long every;

    private long end;
    private long start;
    private long counter;

    private long limiter;

    public Scheduler(@Nonnull JavaPlugin plugin, boolean async) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.async = async;

        this.after = this.counter = 0;
        this.every = this.limiter = this.end = this.start = -1;

        this.freezeFilters = new LinkedList<>();
        this.terminateFilters = new LinkedList<>();
    }

    public int getId() {
        return (this.task != null) ? this.task.getId() : -1;
    }

    public boolean isCancelled() {
        return (this.task != null) && this.task.isCancelled();
    }

    @Nonnull
    public Scheduler async(boolean async) {
        this.async = async;
        return this;
    }

    @Nonnull
    public Scheduler between(long start, long end) {
        this.counter = Math.max(0, start);
        this.start = Math.max(0, start);
        this.end = Math.max(0, end);
        return this;
    }

    @Nonnull
    public Scheduler limit(long limiter) {
        this.limiter = Math.max(limiter, 0);
        return this;
    }

    @Nonnull
    public Scheduler after(long after) {
        this.after = Math.max(after, 0);
        return this;
    }

    @Nonnull
    public Scheduler every(long every) {
        this.every = Math.max(every, -1);
        return this;
    }

    @Nonnull
    public Scheduler after(long after, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        return this.after(timeUnit.toMillis(after) / 50);
    }

    @Nonnull
    public Scheduler every(long every, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        return this.every(timeUnit.toMillis(every) / 50);
    }

    @Nonnull
    public Scheduler after(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        return this.after(duration.toMillis() / 50);
    }

    @Nonnull
    public Scheduler every(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        return this.every(duration.toMillis() / 50);
    }

    @Nonnull
    public Scheduler freezeIf(@Nonnull Function<SchedulerRunnable, Boolean> freezeFilter) {
        this.freezeFilters.add(Validate.notNull(freezeFilter, "freeze filter cannot be null!"));
        return this;
    }

    @Nonnull
    public Scheduler terminateIf(@Nonnull Function<SchedulerRunnable, Boolean> terminateFilter) {
        this.terminateFilters.add(Validate.notNull(terminateFilter, "terminate filter cannot be null!"));
        return this;
    }

    @Nonnull
    public Scheduler whenStarted(@Nonnull Runnable runnable) {
        this.startRunnable = Validate.notNull(runnable, "end runnable cannot be null!");
        return this;
    }

    @Nonnull
    public Scheduler whenEnded(@Nonnull Runnable runnable) {
        this.endRunnable = Validate.notNull(runnable, "end runnable cannot be null!");
        return this;
    }

    @Nonnull
    public synchronized Scheduler cancel() {
        if (this.task != null) this.task.cancel();
        return this;
    }

    @Nonnull
    public synchronized Scheduler run(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "runnable cannot be null!");
        return this.run((task) -> runnable.run());
    }

    @Nonnull
    public synchronized Scheduler run(@Nonnull Consumer<SchedulerRunnable> consumer) {
        Validate.notNull(consumer, "consumer cannot be null!");
        return this.run((task, count) -> consumer.accept(task));
    }

    @Nonnull
    public synchronized Scheduler run(@Nonnull BiConsumer<SchedulerRunnable, Long> consumer) {
        Validate.notNull(consumer, "consumer cannot be null!");

        this.task = new SchedulerRunnable(this.plugin).whenProcessed(() -> {
            for (Function<SchedulerRunnable, Boolean> freezeFilter : this.freezeFilters) {
                if (freezeFilter.apply(this.task)) {
                    return;
                }
            }
            for (Function<SchedulerRunnable, Boolean> terminateFilter : this.terminateFilters) {
                if (terminateFilter.apply(this.task)) {
                    this.task.cancel();
                    return;
                }
            }


            if (this.every == -1) {
                consumer.accept(this.task, this.counter);
                return;
            } else if (this.limiter != -1 && this.limiter-- <= 0) {
                this.task.cancel();
                return;
            }


            if (this.start == -1 && this.end == -1) {
                consumer.accept(this.task, this.counter++);
            } else if (this.start == this.end) {
                consumer.accept(this.task, this.counter++);
                this.task.cancel();
            } else if (this.start < this.end) {
                if (this.counter >= this.start && this.counter <= this.end)
                    consumer.accept(this.task, this.counter++);
                if (this.counter > this.end)
                    this.task.cancel();
            } else {
                if (this.counter <= this.start && this.counter >= this.end)
                    consumer.accept(this.task, this.counter--);
                if (this.counter < this.end)
                    this.task.cancel();
            }
        }).whenStarted(() -> {
            if (this.startRunnable != null)
                this.startRunnable.run();
        }).whenEnded(() -> {
            if (this.endRunnable != null)
                this.endRunnable.run();
        });

        if (!this.async && this.every == -1) this.task.runLater(this.after);
        else if (!this.async) this.task.runTimer(this.after, this.every);
        else if (this.every == -1) this.task.runAsyncLater(this.after);
        else this.task.runAsyncTimer(this.after, this.every);

        return this;
    }
}
