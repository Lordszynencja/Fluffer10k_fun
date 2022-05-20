package bot.commands.rpg.blacksmith.tasks;

import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget;

public class BlacksmithTask {
	public final String id;
	public final BlacksmithTier tier;
	public final String description;
	public final BlacksmithTaskTarget target;

	public BlacksmithTask(final String id, final BlacksmithTier tier, final String description,
			final BlacksmithTaskTarget target) {
		this.id = id;
		this.tier = tier;
		this.description = description;
		this.target = target;
	}
}
