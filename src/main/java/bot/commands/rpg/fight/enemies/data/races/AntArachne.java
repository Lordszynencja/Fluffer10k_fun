package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class AntArachne extends EnemiesOfRace {
	public static final String ANT_ARACHNE_1 = "ANT_ARACHNE_1";

	public AntArachne() {
		super(MonsterGirlRace.ANT_ARACHNE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(ANT_ARACHNE_1)//
				.strength(3).agility(5).intelligence(2)//
				.baseHp(5)//
				.armor(2)//
				.diff(2)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.WRAP_IN_WEB)))//
				.build(rpgEnemies);
	}
}
