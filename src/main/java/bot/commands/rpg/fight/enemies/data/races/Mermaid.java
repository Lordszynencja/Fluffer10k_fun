package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Mermaid extends EnemiesOfRace {
	public static final String MERMAID_1 = "MERMAID_1";
	public static final String MERMAID_2 = "MERMAID_2";

	public Mermaid() {
		super(MonsterGirlRace.MERMAID);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(MERMAID_1)//
				.strength(5).agility(4).intelligence(6)//
				.baseHp(5)//
				.diff(1)//
				.classes(FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.CHARM)) //
						.gentle()));
		rpgEnemies.add(makeBuilder(MERMAID_2)//
				.strength(7).agility(6).intelligence(10)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.CHARM))//
						.gentle()));
	}
}