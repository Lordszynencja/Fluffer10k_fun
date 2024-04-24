package bot.commands.rpg.fight.enemies.races;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Werebat extends EnemiesOfRace {
	public static final String WEREBAT_1 = "WEREBAT_1";

	public Werebat() {
		super(MonsterGirlRace.WEREBAT);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(WEREBAT_1)//
				.strength(1).agility(4).intelligence(2)//
				.baseHp(1)//
				.diff(1)//
				.classes(FighterClass.FLYING, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(data -> RPGFightAction.ATTACK_A_1)//
				.build(rpgEnemies);
	}
}
