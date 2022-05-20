package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class DarkPriest extends EnemiesOfRace {
	public static final String DARK_PRIEST_1 = "DARK_PRIEST_1";
	public static final String DARK_PRIEST_2 = "DARK_PRIEST_2";

	public DarkPriest() {
		super(MonsterGirlRace.DARK_PRIEST);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(DARK_PRIEST_1)//
				.strength(5).agility(4).intelligence(8)//
				.baseHp(10)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
		makeBuilder(DARK_PRIEST_2)//
				.strength(6).agility(5).intelligence(10)//
				.baseHp(15)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}