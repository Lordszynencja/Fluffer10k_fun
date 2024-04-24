package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Apophis extends EnemiesOfRace {
	public Apophis() {
		super(MonsterGirlRace.APOPHIS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.SPELL_FIREBALL), //
				pair(1, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_LIGHTNING), //
				pair(1, RPGFightAction.SPELL_HEAL), //
				pair(1, RPGFightAction.CHARM), //
				pair(2, RPGFightAction.NEUROTOXIN));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.SPELL_FIREBALL), //
				pair(1, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_LIGHTNING), //
				pair(1, RPGFightAction.SPELL_HEAL), //
				pair(1, RPGFightAction.CHARM), //
				pair(1, RPGFightAction.CHARM_SWEET_VOICE), //
				pair(3, RPGFightAction.NEUROTOXIN));
		final ActionSelector actionSelectorC = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.SPELL_FIREBALL), //
				pair(1, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_LIGHTNING), //
				pair(1, RPGFightAction.SPELL_HEAL), //
				pair(1, RPGFightAction.SPELL_QUADRUPLE_FLAME), //
				pair(1, RPGFightAction.CHARM), //
				pair(1, RPGFightAction.CHARM_SWEET_VOICE), //
				pair(4, RPGFightAction.NEUROTOXIN));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 11, 1, 18, 2, 23, 3, 40, 2)//
					.armor(2 + i * 3 / 5)//
					.actionSelector(i < 2 ? actionSelectorA//
							: i < 4 ? actionSelectorB //
									: actionSelectorC)//
					.build(rpgEnemies);
		}
	}
}