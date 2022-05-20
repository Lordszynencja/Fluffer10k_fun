package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Dwarf extends EnemiesOfRace {
	public static final String DWARF_1 = "DWARF_1";
	public static final String DWARF_2 = "DWARF_2";
	public static final String DWARF_3 = "DWARF_3";
	public static final String DWARF_4 = "DWARF_4";

	public static final String DWARF_BLACKSMITH_BOSS = "DWARF_BLACKSMITH_BOSS";

	public Dwarf() {
		super(MonsterGirlRace.DWARF);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(DWARF_1)//
				.strength(14).agility(7).intelligence(7)//
				.baseHp(10)//
				.armor(1)//
				.actionSelector(action(RPGFightAction.ATTACK_S_8))//
				.build(rpgEnemies);
		makeBuilder(DWARF_2)//
				.strength(16).agility(8).intelligence(8)//
				.baseHp(15)//
				.armor(2)//
				.actionSelector(action(RPGFightAction.ATTACK_S_8))//
				.build(rpgEnemies);
		makeBuilder(DWARF_3)//
				.strength(18).agility(9).intelligence(9)//
				.baseHp(20)//
				.armor(3)//
				.actionSelector(action(RPGFightAction.ATTACK_S_8))//
				.build(rpgEnemies);
		makeBuilder(DWARF_4)//
				.strength(20).agility(10).intelligence(10)//
				.baseHp(25)//
				.armor(4)//
				.actionSelector(action(RPGFightAction.ATTACK_S_8))//
				.build(rpgEnemies);

		makeBuilder(DWARF_BLACKSMITH_BOSS).name("Dwarf blacksmith")//
				.strength(7).agility(5).intelligence(3)//
				.baseHp(15)//
				.diff(1)//
				.armor(6)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_8), //
						pair(1, RPGFightAction.ATTACK_HAMMER_SMASH)))//
				.build(rpgEnemies);
	}
}