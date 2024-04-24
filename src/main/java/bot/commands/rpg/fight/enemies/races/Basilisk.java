package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Basilisk extends EnemiesOfRace {
	public static final String BASILISK_1 = "BASILISK_1";

	public Basilisk() {
		super(MonsterGirlRace.BASILISK);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(BASILISK_1)//
				.strength(7).agility(10).intelligence(12)//
				.baseHp(10)//
				.armor(2)//
				.diff(5)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.BASILISK_EYES)))//
				.build(rpgEnemies);
	}
}