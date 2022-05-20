package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.CHARM_FEATHERS;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FIREBALL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIGHTNING;
import static bot.data.fight.FighterClass.FLYING;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class OwlMage extends EnemiesOfRace {
	public static final String OWL_MAGE_0 = "OWL_MAGE_0";
	public static final String OWL_MAGE_1 = "OWL_MAGE_1";
	public static final String OWL_MAGE_2 = "OWL_MAGE_2";
	public static final String OWL_MAGE_3 = "OWL_MAGE_3";
	public static final String OWL_MAGE_4 = "OWL_MAGE_4";
	public static final String OWL_MAGE_5 = "OWL_MAGE_5";

	public OwlMage() {
		super(MonsterGirlRace.OWL_MAGE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, SPELL_FIREBALL), //
				pair(1, SPELL_ICE_BOLT), //
				pair(1, SPELL_LIGHTNING), //
				pair(1, CHARM_FEATHERS));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(5 + i).agility(5 + i).intelligence(15 + i * 3)//
					.baseHp(15 + i * 3)//
					.classes(FLYING, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
