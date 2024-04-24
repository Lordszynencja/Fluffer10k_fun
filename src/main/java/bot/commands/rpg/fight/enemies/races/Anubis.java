package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Anubis extends EnemiesOfRace {
	public Anubis() {
		super(MonsterGirlRace.ANUBIS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 10, 3, 5, 1, 13, 3, 15, 4)//
					.classes(FighterClass.USES_MAGIC)//
					.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
							pair(4, RPGFightAction.ATTACK_S_2), //
							pair(2, RPGFightAction.CURSE), //
							pair(1, RPGFightAction.MUMMY_CURSE)))//
					.build(rpgEnemies);
		}
	}
}