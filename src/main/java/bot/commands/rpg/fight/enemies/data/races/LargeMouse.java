package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class LargeMouse extends EnemiesOfRace {
	public static final String LARGE_MOUSE_1 = "LARGE_MOUSE_1";

	public LargeMouse() {
		super(MonsterGirlRace.LARGE_MOUSE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(LARGE_MOUSE_1)//
				.strength(2).agility(6).intelligence(2)//
				.baseHp(5)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_A_3))));
	}
}