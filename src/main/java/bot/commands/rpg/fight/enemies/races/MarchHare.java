package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class MarchHare extends EnemiesOfRace {
	public static final String MARCH_HARE_1 = "MARCH_HARE_1";

	public MarchHare() {
		super(MonsterGirlRace.MARCH_HARE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(MARCH_HARE_1)//
				.strength(2).agility(3).intelligence(2)//
				.baseHp(5)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}