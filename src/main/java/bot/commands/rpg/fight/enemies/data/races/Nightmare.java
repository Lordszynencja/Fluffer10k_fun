package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;

public class Nightmare extends EnemiesOfRace {
	public static final String NIGHTMARE_1 = "NIGHTMARE_1";
	public static final String NIGHTMARE_2 = "NIGHTMARE_2";

	public Nightmare() {
		super(MonsterGirlRace.NIGHTMARE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final Targetting withSleepTargetting = new Targetting(TargetCheck.ENEMY.with(FighterStatus.SLEEP));
		final Targetting nonMechanicalTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL));

		rpgEnemies.add(makeBuilder(NIGHTMARE_1)//
				.strength(4).agility(3).intelligence(8)//
				.baseHp(-10)//
				.diff(1)//
				.classes(FighterClass.QUICK, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTargetted(//
						data -> RPGFightAction.ATTACK_S_1, //
						pair(withSleepTargetting, data -> RPGFightAction.ATTACK_S_1), //
						pair(nonMechanicalTargetting, data -> RPGFightAction.SPELL_SLEEP))));
		rpgEnemies.add(makeBuilder(NIGHTMARE_2)//
				.strength(6).agility(3).intelligence(12)//
				.baseHp(1)//
				.diff(2)//
				.classes(FighterClass.QUICK, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTargetted(//
						data -> RPGFightAction.ATTACK_S_1, //
						pair(withSleepTargetting,
								data -> data.activeFighter.statuses.isStatus(FighterStatus.MAGIC_SHIELD)
										? RPGFightAction.ATTACK_S_1
										: RPGFightAction.SPELL_MAGIC_SHIELD), //
						pair(nonMechanicalTargetting, data -> RPGFightAction.SPELL_SLEEP))));
	}
}