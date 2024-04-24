package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Dwarf extends EnemiesOfRace {
	public static final String DWARF_BLACKSMITH_BOSS = "DWARF_BLACKSMITH_BOSS";

	public Dwarf() {
		super(MonsterGirlRace.DWARF);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(RPGFightAction.ATTACK_S_8);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(15 + 2 * i).agility(5 + i).intelligence(5 + i)//
					.baseHp(10 + 3 * i)//
					.armor(1 + i * 3 / 5)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}

		makeBuilder(DWARF_BLACKSMITH_BOSS).name("Dwarf blacksmith")//
				.strength(15).agility(5).intelligence(5)//
				.baseHp(15)//
				.armor(6)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(3, RPGFightAction.ATTACK_S_8), //
						pair(1, RPGFightAction.ATTACK_HAMMER_SMASH)))//
				.build(rpgEnemies);
	}
}