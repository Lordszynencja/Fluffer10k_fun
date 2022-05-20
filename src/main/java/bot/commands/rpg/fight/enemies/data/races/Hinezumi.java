package bot.commands.rpg.fight.enemies.data.races;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Hinezumi extends EnemiesOfRace {
	public static final String HINEZUMI_1 = "HINEZUMI_1";

	public Hinezumi() {
		super(MonsterGirlRace.HINEZUMI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(HINEZUMI_1)//
				.strength(7).agility(8).intelligence(4)//
				.baseHp(15)//
				.classes(FighterClass.FIERY, FighterClass.WEAK_TO_COLD)//
				.actionSelector(data -> RPGFightAction.ATTACK_A_4)//
				.build(rpgEnemies);
	}
}