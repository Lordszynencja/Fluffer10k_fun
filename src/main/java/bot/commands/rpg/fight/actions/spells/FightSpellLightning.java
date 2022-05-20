package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.LIGHTNING;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.WET;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellLightning implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellLightning(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, LIGHTNING, 3);
		if (!spellCast) {
			return;
		}

		int baseDamage = 2;
		if (data.targetStats.classes.contains(WET)) {
			baseDamage += 1;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage += 1;
		}

		fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, LIGHTNING, action);

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = LIGHTNING;
		}
	}
}
