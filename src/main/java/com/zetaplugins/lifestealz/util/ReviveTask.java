package com.zetaplugins.lifestealz.util;

import cn.yvmou.ylib.api.scheduler.UniversalTask;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Represents a revive task for a player at a specific location.
 *
 * @param location        The location of the beacon where the revive task is taking place.
 * @param task            The Bukkit task that manages the revive process.
 * @param reviver         The UUID of the player reviving
 * @param target
 * @param start           The timestamp when the revive process started (unix epoch in seconds).
 * @param durationSeconds The duration of the revive process in seconds.
 */
public record ReviveTask(Location location, UniversalTask task, UUID reviver, UUID target, long start, int durationSeconds) {}
