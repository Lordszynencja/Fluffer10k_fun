package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Slime extends EnemiesOfRace {
	public static final String SLIME_1 = "SLIME_1";
	public static final String SLIME_2 = "SLIME_2";

	public Slime() {
		super(MonsterGirlRace.SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(SLIME_1)//
				.strength(2).agility(2).intelligence(1)//
				.baseHp(1)//
				.diff(1)//
				.classes(FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(8, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME))));
		rpgEnemies.add(makeBuilder(SLIME_2)//
				.strength(3).agility(4).intelligence(3)//
				.baseHp(3)//
				.diff(1)//
				.classes(FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME))));
	}
}