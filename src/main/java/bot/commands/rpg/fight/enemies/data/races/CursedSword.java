package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class CursedSword extends EnemiesOfRace {
	public static final String CURSED_SWORD_1 = "CURSED_SWORD_1";
	public static final String CURSED_SWORD_2 = "CURSED_SWORD_2";
	public static final String CURSED_SWORD_3 = "CURSED_SWORD_3";
	public static final String CURSED_SWORD_4 = "CURSED_SWORD_4";

	public CursedSword() {
		super(MonsterGirlRace.CURSED_SWORD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(CURSED_SWORD_1)//
				.strength(10).agility(8).intelligence(4)//
				.baseHp(10)//
				.armor(2)//
				.classes(FighterClass.CURSED_SWORD)//
				.actionSelector(action(RPGFightAction.ATTACK_S_14)//
						.gentle())//
				.build(rpgEnemies);
		makeBuilder(CURSED_SWORD_2)//
				.strength(10).agility(8).intelligence(4)//
				.baseHp(10)//
				.armor(2)//
				.classes(FighterClass.CURSED_SWORD)//
				.actionSelector(action(RPGFightAction.ATTACK_S_14))//
				.build(rpgEnemies);
		makeBuilder(CURSED_SWORD_3)//
				.strength(14).agility(10).intelligence(4)//
				.baseHp(20)//
				.armor(3)//
				.classes(FighterClass.CURSED_SWORD)//
				.actionSelector(action(RPGFightAction.ATTACK_S_14))//
				.build(rpgEnemies);
		makeBuilder(CURSED_SWORD_4)//
				.strength(18).agility(13).intelligence(4)//
				.baseHp(25)//
				.armor(4)//
				.classes(FighterClass.CURSED_SWORD)//
				.actionSelector(action(RPGFightAction.ATTACK_S_14))//
				.build(rpgEnemies);
	}
}
