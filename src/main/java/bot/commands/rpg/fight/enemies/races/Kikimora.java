package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Kikimora extends EnemiesOfRace {
	public static final String KIKIMORA_1 = "KIKIMORA_1";

	public Kikimora() {
		super(MonsterGirlRace.KIKIMORA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(KIKIMORA_1)//
				.strength(2).agility(3).intelligence(3)//
				.baseHp(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(3, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_PURE))//
						.gentle())//
				.build(rpgEnemies);
	}
}