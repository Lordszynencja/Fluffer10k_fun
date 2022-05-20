package bot.commands.rpg.fight.enemies.data.races;

import static bot.data.fight.FighterClass.CANT_BE_FROZEN;
import static bot.data.fight.FighterClass.FREEZING_AURA;
import static bot.data.fight.FighterClass.ICY;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Glacies extends EnemiesOfRace {
	public static final String GLACIES_0 = "GLACIES_0";
	public static final String GLACIES_1 = "GLACIES_1";
	public static final String GLACIES_2 = "GLACIES_2";
	public static final String GLACIES_3 = "GLACIES_3";
	public static final String GLACIES_4 = "GLACIES_4";
	public static final String GLACIES_5 = "GLACIES_5";

	public Glacies() {
		super(MonsterGirlRace.GLACIES);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_BLIZZARD));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(6 + i).agility(7 + i).intelligence(12 + i * 3)//
					.baseHp(10 + i * 4)//
					.armor(2 + i * 3 / 5)//
					.classes(CANT_BE_FROZEN, FREEZING_AURA, ICY, USES_MAGIC, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
