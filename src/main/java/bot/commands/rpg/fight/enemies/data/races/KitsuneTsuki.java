package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class KitsuneTsuki extends EnemiesOfRace {
	public static final String KITSUNE_TSUKI_1 = "KITSUNE_TSUKI_1";
	public static final String KITSUNE_TSUKI_2 = "KITSUNE_TSUKI_2";

	public KitsuneTsuki() {
		super(MonsterGirlRace.KITSUNE_TSUKI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(KITSUNE_TSUKI_1)//
				.strength(3).agility(5).intelligence(8)//
				.baseHp(5)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CHARM))));
		rpgEnemies.add(makeBuilder(KITSUNE_TSUKI_2)//
				.strength(4).agility(7).intelligence(12)//
				.baseHp(15)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.CHARM))));
	}
}