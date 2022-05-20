package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.QUADRUPLE_FLAME;
import static bot.data.fight.FighterClass.FIERY;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellQuadrupleFlame implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellQuadrupleFlame(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, QUADRUPLE_FLAME, 16);
		if (!spellCast) {
			return;
		}

		int baseDamage = 3;
		if (data.targetStats.classes.contains(WEAK_TO_FIRE)) {
			baseDamage += 1;
		}
		if (data.targetStats.classes.contains(FIERY)) {
			baseDamage -= 1;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage -= 1;
		}

		for (int i = 0; i < 4; i++) {
			fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, QUADRUPLE_FLAME, action);
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = QUADRUPLE_FLAME;
		}
	}
}
