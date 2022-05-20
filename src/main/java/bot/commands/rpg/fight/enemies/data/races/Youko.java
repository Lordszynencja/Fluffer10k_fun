package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Youko extends EnemiesOfRace {
	public static final String YOUKO_1 = "YOUKO_1";
	public static final String YOUKO_2 = "YOUKO_2";

	public Youko() {
		super(MonsterGirlRace.YOUKO);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(YOUKO_1)//
				.strength(4).agility(6).intelligence(8)//
				.baseHp(10)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM_KITSUNE)))//
				.build(rpgEnemies);
		makeBuilder(YOUKO_2)//
				.strength(5).agility(8).intelligence(12)//
				.baseHp(20)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM_KITSUNE)))//
				.build(rpgEnemies);
	}
}