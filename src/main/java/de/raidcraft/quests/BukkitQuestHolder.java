package de.raidcraft.quests;

import com.avaje.ebean.EbeanServer;
import de.raidcraft.RaidCraft;
import de.raidcraft.api.quests.QuestException;
import de.raidcraft.quests.api.holder.AbstractQuestHolder;
import de.raidcraft.quests.api.quest.Quest;
import de.raidcraft.quests.api.quest.QuestTemplate;
import de.raidcraft.quests.tables.TPlayer;

import java.util.List;
import java.util.UUID;

/**
 * @author Silthus
 */
public class BukkitQuestHolder extends AbstractQuestHolder {

    public BukkitQuestHolder(int id, UUID playerId) {

        super(id, playerId);
        loadExistingQuests();
    }

    private void loadExistingQuests() {

        QuestManager component = RaidCraft.getComponent(QuestManager.class);
        component.getAllQuests(this).stream()
                .filter(Quest::isActive)
                .forEach(this::addQuest);
    }

    @Override
    public List<Quest> getAllQuests() {

        QuestManager component = RaidCraft.getComponent(QuestManager.class);
        return component.getAllQuests(this);
    }

    @Override
    public Quest startQuest(QuestTemplate template) throws QuestException {

        super.startQuest(template);
        Quest quest = RaidCraft.getComponent(QuestManager.class).createQuest(this, template);
        if (!quest.isActive()) {
            quest.start();
        }
        quest.updateObjectiveListeners();
        addQuest(quest);
        return quest;
    }

    @Override
    public void save() {

        EbeanServer database = RaidCraft.getDatabase(QuestPlugin.class);
        TPlayer player = database.find(TPlayer.class, getId());
        if (player == null) return;
        player.setActiveQuests(getActiveQuests().size());
        player.setCompletedQuests(getCompletedQuests().size());
        database.save(player);
        // also save all quests the player has
        getAllQuests().forEach(Quest::save);
        getQuestInventory().save();
    }
}
