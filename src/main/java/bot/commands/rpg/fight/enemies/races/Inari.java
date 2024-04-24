package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Inari extends EnemiesOfRace {
	public static final String INARI_1 = "INARI_1";
	public static final String INARI_2 = "INARI_2";

	public Inari() {
		super(MonsterGirlRace.INARI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(INARI_1)//
				.strength(3).agility(5).intelligence(6)//
				.baseHp(10)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_KITSUNE)))//
				.build(rpgEnemies);
		makeBuilderOld(INARI_2)//
				.strength(5).agility(7).intelligence(10)//
				.baseHp(15)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_KITSUNE)))//
				.build(rpgEnemies);
	}
}