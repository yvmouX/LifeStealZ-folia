package com.zetaplugins.lifestealz.commands.MainCommand.subcommands;

import cn.yvmou.ylib.api.scheduler.UniversalScheduler;
import cn.yvmou.ylib.api.scheduler.UniversalTask;
import org.bukkit.command.CommandSender;
import com.zetaplugins.lifestealz.LifeStealZ;
import com.zetaplugins.lifestealz.commands.SubCommand;
import com.zetaplugins.lifestealz.util.MessageUtils;
import com.zetaplugins.lifestealz.util.commands.CommandUtils;
import com.zetaplugins.lifestealz.storage.Storage;

import static com.zetaplugins.lifestealz.util.commands.CommandUtils.throwUsageError;

public final class DataSubCommand implements SubCommand {
    private final Storage storage;
    private final LifeStealZ plugin;
    private final UniversalScheduler scheduler = LifeStealZ.getyLib().getScheduler();

    public DataSubCommand(LifeStealZ plugin) {
        this.storage = plugin.getStorage();
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        if (args.length < 3) {
            throwUsageError(sender, "/lifestealz data <import | export> <file>");
            return false;
        }

        String optionTwo = args[1];
        String fileName = args[2];

        if (optionTwo.equals("export")) {
            return handleExport(sender, fileName);
        } else if (optionTwo.equals("import")) {
            return handleImport(sender, fileName);
        } else {
            throwUsageError(sender, getUsage());
        }
        return true;
    }

    private boolean handleExport(CommandSender sender, String fileName) {
        sender.sendMessage(MessageUtils.getAndFormatMsg(
                true,
                "exportingData",
                "&7Exporting player data..."
        ));
        UniversalTask task = scheduler.runAsync(() -> {
            String filePath = storage.export(fileName);
            if (filePath != null) {
                sender.sendMessage(MessageUtils.getAndFormatMsg(
                        true,
                        "exportData",
                        "&7Successfully exported player data to &c%file%",
                        new MessageUtils.Replaceable("%file%", filePath)
                ));
            } else {
                sender.sendMessage(MessageUtils.getAndFormatMsg(
                        false,
                        "exportDataError",
                        "&cFailed to export data! Check console for details."
                ));
            }
        });
        plugin.getAsyncTaskManager().addTask(task);
        return true;
    }

    private boolean handleImport(CommandSender sender, String fileName) {
        sender.sendMessage(MessageUtils.getAndFormatMsg(
                true,
                "importingData",
                "&7Importing player data..."
        ));
        UniversalTask task =scheduler.runAsync(() -> {
            storage.importData(fileName);
            sender.sendMessage(MessageUtils.getAndFormatMsg(
                    true,
                    "importData",
                    "&7Successfully imported &c%file%.csv&7!\n&cPlease restart the server, to ensure flawless migration!",
                    new MessageUtils.Replaceable("%file%", fileName)
            ));
        });
        plugin.getAsyncTaskManager().addTask(task);
        return true;
    }

    @Override
    public String getUsage() {
        return "/lifestealz data <import | export> <file>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lifestealz.managedata");
    }
}