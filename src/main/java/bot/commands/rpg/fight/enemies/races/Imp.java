package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Imp extends EnemiesOfRace {
	public static final String IMP_1 = "IMP_1";
	public static final String IMP_2 = "IMP_2";

	public Imp() {
		super(MonsterGirlRace.IMP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(IMP_1)//
				.strength(1).agility(2).intelligence(2)//
				.baseHp(2)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.CHARM))));
		rpgEnemies.add(makeBuilderOld(IMP_2)//
				.strength(2).agility(3).intelligence(4)//
				.baseHp(5)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.CHARM))));
	}
}