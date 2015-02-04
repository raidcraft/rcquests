package de.raidcraft.quests.api.script.action;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.ConversationHost;
import de.raidcraft.quests.api.InvalidQuestHostException;
import de.raidcraft.quests.api.QuestHost;
import de.raidcraft.quests.api.provider.Quests;
import de.raidcraft.rcconversations.RCConversationsPlugin;
import de.raidcraft.rcconversations.host.PlayerHost;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author mdoering
 */
public class StartConversationAction implements Action<Player> {

    @Override
    public void accept(Player player, ConfigurationSection config) {

        String hostId = config.getString("host");
        String conv = config.getString("conv");
        final ConversationHost host;
        if (hostId == null || hostId.equals("")) {
            host = new PlayerHost(player, conv);
        } else {
            try {
                QuestHost questHost = Quests.getQuestHost(hostId);
                if (questHost instanceof ConversationHost) {
                    host = (ConversationHost) questHost;
                } else {
                    RaidCraft.LOGGER.warning("Defined host is not a quest host!");
                    return;
                }
            } catch (InvalidQuestHostException e) {
                RaidCraft.LOGGER.warning(e.getMessage());
                return;
            }
        }

        host.setConversation(player, conv);
        // reset the conversation to the default conv 1 tick later
        // TODO: hotfix to reset conversation to default one and clear cache
        Bukkit.getScheduler().runTaskLater(RaidCraft.getComponent(RCConversationsPlugin.class),
                () -> host.setConversation(player, host.getDefaultConversationName()), 1L);
    }
}
