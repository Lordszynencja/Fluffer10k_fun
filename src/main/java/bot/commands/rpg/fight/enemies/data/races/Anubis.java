package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Anubis extends EnemiesOfRace {
	public static final String ANUBIS_1 = "ANUBIS_1";

	public Anubis() {
		super(MonsterGirlRace.ANUBIS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(ANUBIS_1)//
				.strength(10).agility(5).intelligence(13)//
				.baseHp(15)//
				.diff(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_2), //
						pair(2, RPGFightAction.CURSE), //
						pair(1, RPGFightAction.MUMMY_CURSE)))//
				.build(rpgEnemies);
	}
}