package com.zetaplugins.lifestealz.util;

import cn.yvmou.ylib.api.scheduler.UniversalTask;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Manages all running async tasks
 */
public final class AsyncTaskManager {
    private final List<UniversalTask> runningTasks = new ArrayList<>();

    /**
     * Add a task to the list of running tasks
     *
     * @param task The task to add
     */
    public void addTask(UniversalTask task) {
        runningTasks.add(task);
    }

    /**
     * Cancel all running tasks
     */
    public void cancelAllTasks() {
        for (UniversalTask task : runningTasks) {
            task.cancel();
        }
        runningTasks.clear();
    }
}