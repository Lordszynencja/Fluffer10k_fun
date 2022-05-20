package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.RAIJU_LIGHTNING;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.WET;
import static bot.data.fight.FighterStatus.PARALYZED;
import static bot.data.fight.FighterStatus.RAIJU_CHARGE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;

public class FightSpellRaijuLightning implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellRaijuLightning(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, RAIJU_LIGHTNING, 5);
		if (!spellCast) {
			return;
		}

		final int stacks = data.activeFighter.statuses.getStacks(RAIJU_CHARGE);
		if (stacks < 5) {
			data.activeFighter.statuses.addStatus(new FighterStatusData(RAIJU_CHARGE).endless().stacks(1));
		}

		int baseDamage = (stacks + 1) / 2;
		if (data.targetStats.classes.contains(WET)) {
			baseDamage += 1;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage += 1;
		}

		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, RAIJU_LIGHTNING, action);

		if (dmg >= 5) {
			data.activeFighter.addExp(10);
			data.target.addExp(10);
			data.target.statuses.addStatus(new FighterStatusData(PARALYZED).duration(2));
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = RAIJU_LIGHTNING;
		}
	}
}
