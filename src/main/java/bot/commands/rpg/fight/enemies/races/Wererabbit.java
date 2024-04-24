package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Wererabbit extends EnemiesOfRace {
	public static final String WERERABBIT_1 = "WERERABBIT_1";

	public Wererabbit() {
		super(MonsterGirlRace.WERERABBIT);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(WERERABBIT_1)//
				.strength(3).agility(5).intelligence(1)//
				.baseHp(3)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1)))//
				.build(rpgEnemies);
	}
}
