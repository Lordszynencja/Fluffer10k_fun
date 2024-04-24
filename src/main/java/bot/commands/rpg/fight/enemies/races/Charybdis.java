package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Charybdis extends EnemiesOfRace {
	public static final String CHARYBDIS_1 = "CHARYBDIS_1";

	public Charybdis() {
		super(MonsterGirlRace.CHARYBDIS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(CHARYBDIS_1)//
				.strength(4).agility(2).intelligence(6)//
				.baseHp(10)//
				.armor(2)//
				.diff(3)//
				.classes(FighterClass.WET, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.SPELL_WHIRLPOOL), //
						pair(1, RPGFightAction.BUBBLES), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
	}
}