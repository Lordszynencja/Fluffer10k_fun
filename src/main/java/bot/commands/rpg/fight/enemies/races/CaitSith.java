package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class CaitSith extends EnemiesOfRace {
	public CaitSith() {
		super(MonsterGirlRace.CAIT_SITH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 4, 1, 5, 3, 10, 4, 10, 5)//
					.classes(FighterClass.USES_MAGIC)//
					.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
							pair(2, RPGFightAction.SPELL_FIREBALL), //
							pair(2, RPGFightAction.SPELL_ICE_BOLT), //
							pair(2, RPGFightAction.SPELL_LIGHTNING), //
							pair(1, RPGFightAction.SPELL_HEAL), //
							pair(1, RPGFightAction.CHARM_CUTE), //
							pair(10, RPGFightAction.SPELL_MAGIC_SHIELD)))//
					.build(rpgEnemies);
		}
	}
}
