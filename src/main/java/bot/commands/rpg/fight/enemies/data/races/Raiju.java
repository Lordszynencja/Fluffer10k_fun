package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Raiju extends EnemiesOfRace {
	public static final String RAIJU_1 = "RAIJU_1";
	public static final String RAIJU_2 = "RAIJU_2";

	public Raiju() {
		super(MonsterGirlRace.RAIJU);
	}

	@Override
	public void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(RAIJU_1)//
				.strength(3).agility(7).intelligence(5)//
				.baseHp(15)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SPELL_RAIJU_LIGHTNING))));
		rpgEnemies.add(makeBuilder(RAIJU_2)//
				.strength(6).agility(11).intelligence(7)//
				.baseHp(20)//
				.diff(4)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SPELL_RAIJU_LIGHTNING))));
	}
}
