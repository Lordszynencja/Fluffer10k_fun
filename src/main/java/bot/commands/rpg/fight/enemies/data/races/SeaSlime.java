package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class SeaSlime extends EnemiesOfRace {
	public static final String SEA_SLIME_1 = "SEA_SLIME_1";

	public SeaSlime() {
		super(MonsterGirlRace.SEA_SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(SEA_SLIME_1)//
				.strength(3).agility(3).intelligence(2)//
				.baseHp(5)//
				.diff(3)//
				.classes(FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(3, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.PARALYZE))//
						.gentle()));
	}
}