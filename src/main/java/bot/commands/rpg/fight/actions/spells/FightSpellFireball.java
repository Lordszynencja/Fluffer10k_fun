package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.FIREBALL;
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

public class FightSpellFireball implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellFireball(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, FIREBALL, 3);
		if (!spellCast) {
			return;
		}

		int baseDamage = 2;
		if (data.targetStats.classes.contains(WEAK_TO_FIRE)) {
			baseDamage += 1;
		}
		if (data.targetStats.classes.contains(FIERY)) {
			baseDamage -= 2;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage -= 1;
		}

		fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, FIREBALL, action);

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = FIREBALL;
		}
	}
}
