package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Nekomata extends EnemiesOfRace {
	public static final String NEKOMATA_1 = "NEKOMATA_1";

	public Nekomata() {
		super(MonsterGirlRace.NEKOMATA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(NEKOMATA_1)//
				.strength(3).agility(5).intelligence(6)//
				.baseHp(10)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(7, RPGFightAction.ATTACK_A_1), //
						pair(2, RPGFightAction.SPELL_FIREBALL), //
						pair(2, RPGFightAction.SPELL_ICE_BOLT), //
						pair(2, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}