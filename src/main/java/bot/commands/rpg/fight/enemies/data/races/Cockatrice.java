package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Cockatrice extends EnemiesOfRace {
	public static final String COCKATRICE_1 = "COCKATRICE_1";

	public Cockatrice() {
		super(MonsterGirlRace.COCKATRICE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(COCKATRICE_1)//
				.strength(2).agility(6).intelligence(4)//
				.baseHp(10)//
				.diff(4)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SPELL_PETRIFY))//
						.gentle())//
				.build(rpgEnemies);
	}
}