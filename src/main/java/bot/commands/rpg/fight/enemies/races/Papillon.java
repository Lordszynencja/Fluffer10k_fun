package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Papillon extends EnemiesOfRace {
	public static final String PAPILLON_1 = "PAPILLON_1";
	public static final String PAPILLON_2 = "PAPILLON_2";

	public Papillon() {
		super(MonsterGirlRace.PAPILLON);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(PAPILLON_1)//
				.strength(1).agility(3).intelligence(4)//
				.baseHp(4)//
				.diff(2)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_WINGS))//
						.gentle()));
		rpgEnemies.add(makeBuilderOld(PAPILLON_2)//
				.strength(2).agility(4).intelligence(6)//
				.baseHp(6)//
				.diff(2)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_WINGS))//
						.gentle()));
	}
}