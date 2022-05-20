package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.WHIRLPOOL;
import static bot.data.fight.FighterClass.FIERY;
import static bot.data.fight.FighterClass.MECHANICAL;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellWhirlpool implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellWhirlpool(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, WHIRLPOOL, 9);
		if (!spellCast) {
			return;
		}

		int baseDamage = 3;
		if (data.targetStats.classes.contains(FIERY)) {
			baseDamage += 2;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage += 1;
		}

		fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, WHIRLPOOL, action);

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = WHIRLPOOL;
		}
	}
}