package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Arachne extends EnemiesOfRace {
	public static final String ARACHNE_0 = "ARACHNE_0";
	public static final String ARACHNE_1 = "ARACHNE_1";
	public static final String ARACHNE_2 = "ARACHNE_2";
	public static final String ARACHNE_3 = "ARACHNE_3";
	public static final String ARACHNE_4 = "ARACHNE_4";
	public static final String ARACHNE_5 = "ARACHNE_5";

	public Arachne() {
		super(MonsterGirlRace.ARACHNE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_S_7), //
				pair(1, RPGFightAction.WRAP_IN_WEB));
		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(9 + i).agility(13 + 3 * i).intelligence(5 + i)//
					.baseHp(15 + i * 3)//
					.armor(2 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}