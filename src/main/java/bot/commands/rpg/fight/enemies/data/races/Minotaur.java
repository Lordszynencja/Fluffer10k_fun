package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Minotaur extends EnemiesOfRace {
	public static final String MINOTAUR_1 = "MINOTAUR_1";
	public static final String MINOTAUR_2 = "MINOTAUR_2";

	public Minotaur() {
		super(MonsterGirlRace.MINOTAUR);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(MINOTAUR_1)//
				.strength(12).agility(5).intelligence(3)//
				.baseHp(20)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY)))//
				.build(rpgEnemies);
		makeBuilder(MINOTAUR_2)//
				.strength(18).agility(6).intelligence(3)//
				.baseHp(30)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY)))//
				.build(rpgEnemies);
	}
}