package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_14;
import static bot.commands.rpg.fight.RPGFightAction.FRENZY;
import static bot.data.fight.FighterClass.OCELOMEH_SWORD;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Ocelomeh extends EnemiesOfRace {
	public static final String OCELOMEH_0 = "OCELOMEH_0";
	public static final String OCELOMEH_1 = "OCELOMEH_1";
	public static final String OCELOMEH_2 = "OCELOMEH_2";
	public static final String OCELOMEH_3 = "OCELOMEH_3";
	public static final String OCELOMEH_4 = "OCELOMEH_4";
	public static final String OCELOMEH_5 = "OCELOMEH_5";

	public Ocelomeh() {
		super(MonsterGirlRace.OCELOMEH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_14), //
				pair(1, FRENZY));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(15 + i * 3).agility(10 + i * 3).intelligence(5 + i)//
					.baseHp(20 + i * 5)//
					.classes(OCELOMEH_SWORD)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}