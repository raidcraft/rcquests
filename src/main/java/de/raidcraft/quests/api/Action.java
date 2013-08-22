package de.raidcraft.quests.api;

import de.raidcraft.api.quests.QuestException;
import org.bukkit.entity.Player;

/**
 * @author Silthus
 */
public interface Action<T> {

    public int getId();

    public String getName();

    public T getProvider();

    public boolean isExecutedOnce();

    public void execute(Player player, T holder) throws QuestException;

    public void save();
}
