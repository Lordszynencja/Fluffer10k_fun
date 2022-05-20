package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.PRECISE_STRIKE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.actions.physicalAttacks.FightAttack;

public class FightSpecialAttackPreciseStrike implements FightActionHandler {
	private static final Targetting defaultTargetting = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;
	private final FightAttack fightAttack;

	public FightSpecialAttackPreciseStrike(final Fluffer10kFun fluffer10kFun, final FightAttack fightAttack) {
		this.fluffer10kFun = fluffer10kFun;
		this.fightAttack = fightAttack;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, PRECISE_STRIKE, 3);
		if (!spellCast) {
			return;
		}

		fightAttack.handle(data, action);
	}
}
