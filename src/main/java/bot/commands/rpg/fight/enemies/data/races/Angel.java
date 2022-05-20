package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Angel extends EnemiesOfRace {
	public static final String ANGEL_1 = "ANGEL_1";

	public Angel() {
		super(MonsterGirlRace.ANGEL);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(ANGEL_1)//
				.strength(3).agility(4).intelligence(2)//
				.baseHp(5)//
				.diff(2)//
				.classes(FighterClass.FLYING)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1)))//
				.build(rpgEnemies);
	}
}