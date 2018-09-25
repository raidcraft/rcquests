package de.raidcraft.quests.api.objective;

import de.raidcraft.api.action.TriggerFactory;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.quests.api.quest.QuestTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Silthus
 */
@Data
@EqualsAndHashCode(of = {"id", "questTemplate"})
@ToString(exclude = {"requirements", "trigger", "actions", "tasks"})
public abstract class AbstractObjectiveTemplate implements ObjectiveTemplate {

    private final int id;
    private final String friendlyName;
    private final String description;
    private final boolean optional;
    private final boolean hidden;
    private final boolean autoCompleting;
    private final boolean silent;
    private final int requiredTaskCount;
    private final QuestTemplate questTemplate;
    private final Collection<Requirement<Player>> requirements;
    private final Collection<TriggerFactory> trigger;
    private final Collection<Action<Player>> actions;
    private final Collection<TaskTemplate> tasks;

    public AbstractObjectiveTemplate(int id, QuestTemplate questTemplate, ConfigurationSection data) {

        this.id = id;
        this.friendlyName = data.getString("name");
        this.description = data.getString("desc");
        this.optional = data.getBoolean("optional", false);
        this.hidden = data.getBoolean("hidden", false);
        this.silent = data.getBoolean("silent", false);
        this.requiredTaskCount = data.getInt("required-tasks", 0);
        this.autoCompleting = data.getBoolean("auto-complete", true);
        this.questTemplate = questTemplate;
        this.requirements = loadRequirements(data.getConfigurationSection("requirements"));
        this.trigger = loadTrigger(data.getConfigurationSection("trigger"));
        this.actions = loadActions(data.getConfigurationSection("actions"));
        this.tasks = loadTasks(data.getConfigurationSection("tasks"));
    }

    protected abstract Collection<Requirement<Player>> loadRequirements(ConfigurationSection data);

    protected abstract Collection<TriggerFactory> loadTrigger(ConfigurationSection data);

    protected abstract Collection<Action<Player>> loadActions(ConfigurationSection data);

    protected abstract Collection<TaskTemplate> loadTasks(ConfigurationSection data);

    @Override
    public int compareTo(@NonNull ObjectiveTemplate o) {

        if (getId() < o.getId()) {
            return -1;
        }
        if (getId() > o.getId()) {
            return 1;
        }
        return 0;
    }
}
