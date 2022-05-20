package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_HEAL_SISTERS;
import static bot.data.fight.FighterClass.TRIPLE_ATTACK;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.RandomUtils.getRandomBoolean;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Kamaitachi extends EnemiesOfRace {
	public static final String KAMAITACHI_0 = "KAMAITACHI_0";
	public static final String KAMAITACHI_1 = "KAMAITACHI_1";
	public static final String KAMAITACHI_2 = "KAMAITACHI_2";
	public static final String KAMAITACHI_3 = "KAMAITACHI_3";
	public static final String KAMAITACHI_4 = "KAMAITACHI_4";
	public static final String KAMAITACHI_5 = "KAMAITACHI_5";

	public Kamaitachi() {
		super(MonsterGirlRace.KAMAITACHI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = data -> {
			if (data.fight.actionsLeft == 3) {
				return getRandomBoolean(0.25) ? GRAB : ATTACK_S_1;
			}

			if (data.fight.actionsLeft == 2) {
				return ATTACK_A_1;
			}
			if (data.fight.actionsLeft == 1) {
				return getRandomBoolean(0.33) ? SPELL_HEAL_SISTERS : ATTACK_A_1;
			}

			return ATTACK_S_1;
		};

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(5 + i * 2).agility(5 + i * 2).intelligence(5 + i)//
					.baseHp(15 + i * 3)//
					.diff(5 + i)//
					.classes(TRIPLE_ATTACK, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}