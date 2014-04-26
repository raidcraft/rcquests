package de.raidcraft.quests.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.api.quests.QuestException;
import de.raidcraft.api.quests.QuestTrigger;
import de.raidcraft.api.quests.Quests;
import de.raidcraft.api.quests.player.QuestHolder;
import de.raidcraft.api.quests.quest.Quest;
import de.raidcraft.api.quests.quest.QuestTemplate;
import de.raidcraft.quests.QuestPlugin;
import de.raidcraft.util.PaginatedResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Silthus
 */
public class AdminCommands {

    private final QuestPlugin plugin;

    public AdminCommands(QuestPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"reload"},
            desc = "Reloads the quest plugin"
    )
    @CommandPermissions("rcquests.admin.reload")
    public void reload(CommandContext args, CommandSender sender) {

        plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "Reloaded Quest Plugin sucessfully!");
    }

    @Command(
            aliases = {"accept", "a"},
            desc = "Accepts a quest",
            min = 1
    )
    @CommandPermissions("rcquests.admin.accept")
    public void accept(CommandContext args, CommandSender sender) throws CommandException {

        try {
            QuestTemplate questTemplate = plugin.getQuestManager().getQuestTemplate(args.getString(0));
            QuestHolder player = plugin.getQuestManager().getQuestHolder((Player) sender);
            if (questTemplate == null) {
                throw new CommandException("Unknown quest: " + args.getString(0));
            }
            player.startQuest(questTemplate);
        } catch (QuestException e) {
            throw new CommandException(e);
        }
    }

    @Command(
            aliases = {"list"},
            desc = "Lists trigger, actions and stuff",
            min = 1,
            flags = "p:",
            usage = "[actions|requirements|trigger]"
    )
    public void list(CommandContext args, CommandSender sender) throws CommandException {

        String keyword = args.getString(0);
        List<String> content = new ArrayList<>();
        if (keyword.equalsIgnoreCase("actions")) {
            content = plugin.getQuestManager().getLoadedActions();
        } else if (keyword.equalsIgnoreCase("requirements")) {
            content = plugin.getQuestManager().getLoadedRequirements();
        } else if (keyword.equalsIgnoreCase("trigger")) {
            for (QuestTrigger trigger : Quests.getLoadedTrigger()) {
                content.add(trigger.getName());
            }
        }
        new PaginatedResult<String>("Type Name") {
            @Override
            public String format(String entry) {

                return ChatColor.YELLOW + entry;
            }
        }.display(sender, content, args.getFlagInteger('p', 1));
    }

    @Command(
            aliases = {"abort", "abbruch", "abrechen", "cancel"},
            desc = "Cancels the quest",
            min = 1,
            usage = "[Player] <Quest>"
    )
         @CommandPermissions("rcquests.admin.abort")
         public void abort(CommandContext args, CommandSender sender) throws CommandException {

        Player targetPlayer = (Player)sender;
        String questName = args.getString(0);
        if(args.argsLength() > 1) {
            questName = args.getString(1);
            targetPlayer = Bukkit.getPlayer(args.getString(0));
            if(targetPlayer == null) {
                throw new CommandException("Der angegebene Spieler ist nicht Online!");
            }
        }

        try {
            QuestHolder questPlayer = plugin.getQuestManager().getQuestHolder(targetPlayer);
            Quest quest = questPlayer.getQuest(questName);
            quest.abort();
            sender.sendMessage(ChatColor.GREEN + "Die Quest '" + quest.getFriendlyName() + "' wurde abgebrochen!");
        } catch (QuestException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
