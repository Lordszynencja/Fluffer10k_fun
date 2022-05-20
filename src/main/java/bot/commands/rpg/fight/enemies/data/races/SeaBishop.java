package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class SeaBishop extends EnemiesOfRace {
	public static final String SEA_BISHOP_1 = "SEA_BISHOP_1";
	public static final String SEA_BISHOP_2 = "SEA_BISHOP_2";

	public SeaBishop() {
		super(MonsterGirlRace.SEA_BISHOP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(SEA_BISHOP_1)//
				.strength(4).agility(2).intelligence(6)//
				.baseHp(10)//
				.classes(FighterClass.USES_MAGIC, FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.GRAB)) //
						.gentle()));
		rpgEnemies.add(makeBuilder(SEA_BISHOP_2)//
				.strength(5).agility(4).intelligence(9)//
				.baseHp(10)//
				.classes(FighterClass.USES_MAGIC, FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(5, RPGFightAction.ATTACK_S_7), //
						pair(2, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.SPELL_WHIRLPOOL)) //
						.gentle()));
	}
}