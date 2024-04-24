package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Barometz extends EnemiesOfRace {
	public static final String BAROMETZ_1 = "BAROMETZ_1";

	public Barometz() {
		super(MonsterGirlRace.BAROMETZ);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(BAROMETZ_1)//
				.strength(2).agility(2).intelligence(6)//
				.baseHp(5)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.BAROMETZ_COTTON), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}