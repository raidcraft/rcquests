package de.raidcraft.quests.api;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Silthus
 */
public abstract class AbstractRequirement implements Requirement {

    private final int id;
    private final String type;

    public AbstractRequirement(int id, ConfigurationSection data) {

        this.id = id;
        this.type = data.getString("type");
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public String getType() {

        return type;
    }
}
