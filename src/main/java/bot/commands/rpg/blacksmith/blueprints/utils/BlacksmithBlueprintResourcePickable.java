package bot.commands.rpg.blacksmith.blueprints.utils;

public abstract class BlacksmithBlueprintResourcePickable implements BlacksmithBlueprintResource {
	@Override
	public final boolean isPickable() {
		return true;
	}

	@Override
	public Payer getPayer() {
		return null;
	}
}
