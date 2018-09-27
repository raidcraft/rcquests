package de.raidcraft.quests.api.objective;

import de.raidcraft.api.action.TriggerFactory;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.quests.api.quest.QuestTemplate;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Silthus
 */
public interface ObjectiveTemplate extends Comparable<ObjectiveTemplate> {

    /**
     * The path id of the task.
     *
     * @return id of the task
     */
    int getId();

    /**
     * The name that is displayed in the quest log.
     *
     * @return task name in the quest log
     */
    String getFriendlyName();

    /**
     * Gets the description of the task that is shown in the quest log.
     *
     * @return detailed description in the quest log
     */
    String getDescription();

    /**
     * Optional tasks do not count to the auto completion of the objective.
     *
     * @return true if the task is optional.
     */
    boolean isOptional();

    boolean isHidden();

    boolean isSilent();

    int getRequiredTaskCount();

    /**
     * Auto Completing tasks need to be completed manually by calling the !task.complete action.
     *
     * @return if task is auto completing
     */
    boolean isAutoCompleting();

    /**
     * Gets the {@link QuestTemplate} that is associated with this task.
     *
     * @return parent quest template
     */
    QuestTemplate getQuestTemplate();

    Collection<Requirement<Player>> getRequirements();

    Collection<TriggerFactory> getTrigger();

    Collection<Action<Player>> getActions();

    Collection<Action<Player>> getStartActions();

    Collection<TaskTemplate> getTasks();
}
