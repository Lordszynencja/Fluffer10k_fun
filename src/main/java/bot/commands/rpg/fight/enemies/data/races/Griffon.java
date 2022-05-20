package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Griffon extends EnemiesOfRace {
	public static final String GRIFFON_1 = "GRIFFON_1";
	public static final String GRIFFON_2 = "GRIFFON_2";
	public static final String GRIFFON_3 = "GRIFFON_3";
	public static final String GRIFFON_4 = "GRIFFON_4";

	public Griffon() {
		super(MonsterGirlRace.GRIFFON);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(GRIFFON_1)//
				.strength(10).agility(14).intelligence(9)//
				.baseHp(20)//
				.diff(2)//
				.classes(FighterClass.FLYING)//
				.actionSelector(action(RPGFightAction.ATTACK_A_1))//
				.build(rpgEnemies);
		makeBuilder(GRIFFON_2)//
				.strength(12).agility(17).intelligence(10)//
				.baseHp(25)//
				.diff(2)//
				.classes(FighterClass.FLYING)//
				.actionSelector(action(RPGFightAction.ATTACK_A_1))//
				.build(rpgEnemies);
		makeBuilder(GRIFFON_3)//
				.strength(14).agility(19).intelligence(11)//
				.baseHp(30)//
				.diff(2)//
				.classes(FighterClass.FLYING)//
				.actionSelector(action(RPGFightAction.ATTACK_A_1))//
				.build(rpgEnemies);
		makeBuilder(GRIFFON_4)//
				.strength(15).agility(20).intelligence(12)//
				.baseHp(35)//
				.diff(2)//
				.classes(FighterClass.FLYING)//
				.actionSelector(action(RPGFightAction.ATTACK_A_1))//
				.build(rpgEnemies);
	}
}
