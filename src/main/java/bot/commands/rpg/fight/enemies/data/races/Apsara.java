package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Apsara extends EnemiesOfRace {
	public static final String APSARA_1 = "APSARA_1";
	public static final String APSARA_2 = "APSARA_2";

	public Apsara() {
		super(MonsterGirlRace.APSARA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(APSARA_1)//
				.strength(4).agility(6).intelligence(5)//
				.baseHp(5)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
		makeBuilder(APSARA_2)//
				.strength(5).agility(7).intelligence(7)//
				.baseHp(10)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}
