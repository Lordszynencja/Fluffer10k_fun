package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_HEAL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_MAGIC_SHIELD;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.advancedSpells;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.basicSpells;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.masterSpells;
import static bot.data.fight.FighterClass.CLEVER;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.data.items.loot.LootTable.listT;
import static bot.data.items.loot.LootTable.single;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class CuSith extends EnemiesOfRace {
	public CuSith() {
		super(MonsterGirlRace.CU_SITH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables(//
				pair(1, single(ATTACK_S_2)), //
				pair(1, basicSpells), //
				pair(1, single(CHARM)));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables(//
				pair(2, weightedI(//
						pair(1, SPELL_HEAL), //
						pair(1, SPELL_MAGIC_SHIELD), //
						pair(2, CHARM))), //
				pair(2, basicSpells), //
				pair(1, advancedSpells));
		final ActionSelector actionSelectorC = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables(//
				pair(1, weightedI(//
						pair(1, SPELL_HEAL), //
						pair(1, SPELL_MAGIC_SHIELD), //
						pair(2, CHARM))), //
				pair(1, listT(basicSpells, advancedSpells)));

		final Targetting withoutCharmTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL)//
						.without(FighterStatus.CHARMED)//
						.withStacksLessThan(FighterStatus.CHARM_RESISTANCE, 3));

		final ActionSelector offensiveSpellSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables(//
				pair(2, advancedSpells), //
				pair(1, masterSpells));

		final ActionSelector actionSelectorD = data -> {
			if (data.activeFighter.hp < 10) {
				data.targetId = data.activeFighter.id;
				return SPELL_HEAL;
			}

			final FighterData target = withoutCharmTargetting.getFirst(data.fight, data.activeFighter);
			if (target != null) {
				data.targetId = target.id;
				return CHARM;
			}

			if (!data.activeFighter.statuses.isStatus(FighterStatus.MAGIC_SHIELD)) {
				return SPELL_MAGIC_SHIELD;
			}

			return offensiveSpellSelector.select(data);
		};

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(5).agility(5 + 2 * i).intelligence(10 + 3 * i)//
					.baseHp(10 + i)//
					.classes(USES_MAGIC, i >= 5 ? CLEVER : null)//
					.actionSelector(i < 2 ? actionSelectorA //
							: i < 4 ? actionSelectorB//
									: i < 5 ? actionSelectorC//
											: actionSelectorD)//
					.build(rpgEnemies);
		}
	}
}