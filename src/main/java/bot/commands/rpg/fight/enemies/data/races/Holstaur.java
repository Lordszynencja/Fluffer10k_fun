package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Holstaur extends EnemiesOfRace {
	public static final String HOLSTAUR_1 = "HOLSTAUR_1";
	public static final String HOLSTAUR_2 = "HOLSTAUR_2";

	public Holstaur() {
		super(MonsterGirlRace.HOLSTAUR);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(HOLSTAUR_1)//
				.strength(7).agility(3).intelligence(2)//
				.baseHp(20)//
				.armor(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1))));
		rpgEnemies.add(makeBuilder(HOLSTAUR_2)//
				.strength(10).agility(4).intelligence(3)//
				.baseHp(25)//
				.armor(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1))));
	}
}