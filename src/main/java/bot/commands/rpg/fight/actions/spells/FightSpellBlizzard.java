package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.fight.actions.FightActionUtils.addFreezingStack;
import static bot.commands.rpg.spells.ActiveSkill.BLIZZARD;
import static bot.data.fight.FighterClass.WEAK_TO_COLD;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellBlizzard implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellBlizzard(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, BLIZZARD, 12);
		if (!spellCast) {
			return;
		}

		int baseDamage = 4;
		if (data.targetStats.classes.contains(WEAK_TO_COLD)) {
			baseDamage += 2;
		}

		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, BLIZZARD, action);

		if (dmg > 0) {
			addFreezingStack(data);
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = BLIZZARD;
		}
	}
}
