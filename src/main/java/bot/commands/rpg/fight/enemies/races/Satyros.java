package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Satyros extends EnemiesOfRace {
	public static final String SATYROS_1 = "SATYROS_1";

	public Satyros() {
		super(MonsterGirlRace.SATYROS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(SATYROS_1)//
				.strength(3).agility(4).intelligence(6)//
				.baseHp(10)//
				.diff(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(6, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_MUSIC), //
						pair(1, RPGFightAction.INTOXICATE)))//
				.build(rpgEnemies);
	}
}