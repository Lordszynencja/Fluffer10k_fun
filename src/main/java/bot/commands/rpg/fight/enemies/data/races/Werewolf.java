package bot.commands.rpg.fight.enemies.data.races;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Werewolf extends EnemiesOfRace {
	public static final String WEREWOLF_1 = "WEREWOLF_1";
	public static final String WEREWOLF_2 = "WEREWOLF_2";
	public static final String WEREWOLF_3 = "WEREWOLF_3";

	public Werewolf() {
		super(MonsterGirlRace.WEREWOLF);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(WEREWOLF_1)//
				.strength(5).agility(6).intelligence(5)//
				.baseHp(10)//
				.actionSelector(data -> RPGFightAction.ATTACK_S_2)//
				.build(rpgEnemies);
		makeBuilder(WEREWOLF_2)//
				.strength(7).agility(7).intelligence(6)//
				.baseHp(15)//
				.actionSelector(data -> RPGFightAction.ATTACK_S_2)//
				.build(rpgEnemies);
		makeBuilder(WEREWOLF_3)//
				.strength(9).agility(8).intelligence(7)//
				.baseHp(20)//
				.actionSelector(data -> RPGFightAction.ATTACK_S_2)//
				.build(rpgEnemies);
	}
}
