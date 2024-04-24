package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class ParasiteSlimeSlimeCarrier extends EnemiesOfRace {
	public static final String PARASITE_SLIME_SLIME_CARRIER_1 = "PARASITE_SLIME_SLIME_CARRIER_1";
	public static final String PARASITE_SLIME_SLIME_CARRIER_2 = "PARASITE_SLIME_SLIME_CARRIER_2";

	public ParasiteSlimeSlimeCarrier() {
		super(MonsterGirlRace.PARASITE_SLIME_SLIME_CARRIER);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(PARASITE_SLIME_SLIME_CARRIER_1)//
				.strength(5).agility(3).intelligence(6)//
				.baseHp(20)//
				.diff(7)//
				.classes(FighterClass.DOUBLE_ATTACK, FighterClass.SLIME_REGEN, FighterClass.WEAK_TO_FIRE,
						FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME))));
		rpgEnemies.add(makeBuilderOld(PARASITE_SLIME_SLIME_CARRIER_2)//
				.strength(7).agility(4).intelligence(7)//
				.baseHp(30)//
				.diff(10)//
				.classes(FighterClass.DOUBLE_ATTACK, FighterClass.SLIME_REGEN, FighterClass.WEAK_TO_FIRE,
						FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(3, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME))));
	}
}