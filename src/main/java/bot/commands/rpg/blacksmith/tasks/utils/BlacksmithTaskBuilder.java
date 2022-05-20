package bot.commands.rpg.blacksmith.tasks.utils;

import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTask;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTasksList;

public class BlacksmithTaskBuilder {
	private final String id;
	private BlacksmithTier tier;
	private String description;
	private BlacksmithTaskTarget target;

	public BlacksmithTaskBuilder(final String id) {
		this.id = id;
	}

	public BlacksmithTaskBuilder tier(final BlacksmithTier x) {
		tier = x;
		return this;
	}

	public BlacksmithTaskBuilder description(final String x) {
		description = x;
		return this;
	}

	public BlacksmithTaskBuilder target(final BlacksmithTaskTarget x) {
		target = x;
		return this;
	}

	public void add() {
		BlacksmithTasksList.addTask(new BlacksmithTask(id, tier, description, target));
	}
}
