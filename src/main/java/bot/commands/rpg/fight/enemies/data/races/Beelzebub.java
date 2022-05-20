package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Beelzebub extends EnemiesOfRace {
	public static final String BEELZEBUB_1 = "BEELZEBUB_1";

	public Beelzebub() {
		super(MonsterGirlRace.BEELZEBUB);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(BEELZEBUB_1)//
				.strength(3).agility(11).intelligence(10)//
				.baseHp(10)//
				.armor(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.LICK)))//
				.build(rpgEnemies);
	}
}
