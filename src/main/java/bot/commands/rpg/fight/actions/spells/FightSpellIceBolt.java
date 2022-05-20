package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.fight.actions.FightActionUtils.addFreezingStack;
import static bot.commands.rpg.spells.ActiveSkill.ICE_BOLT;
import static bot.data.fight.FighterClass.WEAK_TO_COLD;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellIceBolt implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellIceBolt(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, ICE_BOLT, 3);
		if (!spellCast) {
			return;
		}

		int baseDamage = 2;
		if (data.targetStats.classes.contains(WEAK_TO_COLD)) {
			baseDamage += 1;
		}

		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, ICE_BOLT, action);

		if (dmg > 0) {
			addFreezingStack(data);
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = ICE_BOLT;
		}
	}
}
