package bot.commands.rpg.fight;

import static java.util.stream.Collectors.toList;

import java.util.List;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemyData.EnemyType;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FightData;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;

public class Targetting {
	public interface TargetCheck {
		public static TargetCheck SELF = (fighter, target) -> fighter.id.equals(target.id);
		public static TargetCheck ALLY = (fighter, target) -> fighter.team.equals(target.team);
		public static TargetCheck ENEMY = (fighter, target) -> !fighter.team.equals(target.team);

		boolean valid(FighterData fighter, FighterData target);

		default TargetCheck damaged() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.hp < target.maxHp;
		}

		default TargetCheck alive() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& !target.isOut();
		}

		default TargetCheck held() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.heldBy != null;
		}

		default TargetCheck notHeld() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.heldBy == null;
		}

		default TargetCheck with(final FighterStatus status) {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.statuses.isStatus(status);
		}

		default TargetCheck without(final FighterStatus status) {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& !target.statuses.isStatus(status);
		}

		default TargetCheck withStacksLessThan(final FighterStatus status, final int maxStacks) {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.statuses.getStacks(status) < maxStacks;
		}

		default TargetCheck with(final Fluffer10kFun fluffer10kFun, final FighterClass fighterClass) {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& fluffer10kFun.rpgStatUtils.getClasses(target).contains(fighterClass);
		}

		default TargetCheck without(final Fluffer10kFun fluffer10kFun, final FighterClass fighterClass) {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& !fluffer10kFun.rpgStatUtils.getClasses(target).contains(fighterClass);
		}

		default TargetCheck player() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.type == FighterType.PLAYER;
		}

		default TargetCheck enemyType() {
			final TargetCheck current = this;
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.type == FighterType.ENEMY;
		}

		default TargetCheck monsterGirl(final Fluffer10kFun fluffer10kFun) {
			final TargetCheck current = enemyType();
			return (fighter, target) -> current.valid(fighter, target)//
					&& target.enemy().enemyData(fluffer10kFun).type == EnemyType.MONSTER_GIRL;
		}

		default TargetCheck monsterGirlOfRaceFrom(final Fluffer10kFun fluffer10kFun,
				final List<MonsterGirlRace> races) {
			final TargetCheck current = monsterGirl(fluffer10kFun);
			return (fighter, target) -> current.valid(fighter, target)//
					&& races.contains(target.enemy().enemyData(fluffer10kFun).mg().race);
		}
	}

	public final TargetCheck check;

	public Targetting(final TargetCheck check) {
		this.check = check;
	}

	public boolean valid(final FighterData fighter, final FighterData target) {
		return check.valid(fighter, target);
	}

	public FighterData getFirst(final FightData fight, final FighterData fighter) {
		for (final FighterData target : fight.fighters.values()) {
			if (check.valid(fighter, target)) {
				return target;
			}
		}

		return null;
	}

	public List<FighterData> getAll(final FightData fight, final FighterData fighter) {
		return fight.fighters.values().stream().filter(target -> check.valid(fighter, target)).collect(toList());
	}

	public Targetting orElse(final TargetCheck otherCheck) {
		final Targetting previous = this;
		return new Targetting(otherCheck) {
			@Override
			public boolean valid(final FighterData fighter, final FighterData target) {
				return previous.valid(fighter, target) || check.valid(fighter, target);
			}

			@Override
			public FighterData getFirst(final FightData fight, final FighterData fighter) {
				final FighterData target = previous.getFirst(fight, fighter);
				return target == null ? super.getFirst(fight, fighter) : target;
			}

			@Override
			public List<FighterData> getAll(final FightData fight, final FighterData fighter) {
				final List<FighterData> targets = previous.getAll(fight, fighter);
				return targets.isEmpty() ? super.getAll(fight, fighter) : targets;
			}
		};
	}
}
