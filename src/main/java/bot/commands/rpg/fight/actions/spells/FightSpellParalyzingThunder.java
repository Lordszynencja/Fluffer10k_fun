package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.PARALYZING_THUNDER;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.WET;
import static bot.data.fight.FighterStatus.PARALYZED;
import static bot.util.RandomUtils.getRandomBoolean;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;

public class FightSpellParalyzingThunder implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellParalyzingThunder(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, PARALYZING_THUNDER, 5);
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

		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, PARALYZING_THUNDER, action);

		if (getRandomBoolean(dmg / (dmg + 1.0))) {
			data.target.statuses.addStatus(new FighterStatusData(PARALYZED).duration(1));
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = PARALYZING_THUNDER;
		}
	}
}