package bot.commands.rpg.fight.enemies.races;

import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class DarkElf extends EnemiesOfRace {
	public DarkElf() {
		super(MonsterGirlRace.DARK_ELF);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_S_10), //
				pair(1, RPGFightAction.SPELL_FIREBALL), //
				pair(1, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_LIGHTNING), //
				pair(3, RPGFightAction.CHARM_CUTE));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, RPGFightAction.ATTACK_S_10), //
				pair(1, RPGFightAction.SPELL_FIREBALL), //
				pair(1, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_LIGHTNING), //
				pair(1, RPGFightAction.SPELL_HEAL), //
				pair(3, RPGFightAction.CHARM_CUTE));

		final Targetting withoutCharmTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL)//
						.without(FighterStatus.CHARMED)//
						.withStacksLessThan(FighterStatus.CHARM_RESISTANCE, 3));

		final RPGFightAction[] attacks = { //
				RPGFightAction.ATTACK_S_10, //
				RPGFightAction.SPELL_FIREBALL, //
				RPGFightAction.SPELL_ICE_BOLT, //
				RPGFightAction.SPELL_LIGHTNING };

		final ActionSelector actionSelectorC = data -> {
			if (data.activeFighter.hp < 15) {
				data.targetId = data.activeFighter.id;
				return RPGFightAction.SPELL_HEAL;
			}

			final FighterData target = withoutCharmTargetting.getFirst(data.fight, data.activeFighter);
			if (target != null) {
				data.targetId = target.id;
				return RPGFightAction.CHARM_CUTE;
			}

			return getRandom(attacks);
		};

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(5 + i).agility(10 + 2 * i).intelligence(5 + 2 * i)//
					.baseHp(10 + 2 * i)//
					.diff(i < 4 ? 0 : 10)//
					.classes(FighterClass.USES_MAGIC)//
					.actionSelector(i < 2 ? actionSelectorA//
							: i < 4 ? actionSelectorB//
									: actionSelectorC)//
					.build(rpgEnemies);
		}
	}
}