package bot.commands.rpg.spells;

import static bot.util.Utils.joinNames;

import java.util.ArrayList;
import java.util.List;

import bot.commands.rpg.fight.RPGFightAction;

public enum ActiveSkill {

	FORCE_HIT(1, "1", SpecialActionType.SPELL, 0, 0, 0, "Force hit", "Deals small magic damage"), //
	GOUGE(1, "2", SpecialActionType.SPECIAL_ATTACK, 0, 0, 0, "Gouge",
			"Physical attack that gouges enemy, making them bleed"), //
	HEAL(1, "3", SpecialActionType.SPELL, 0, 0, 1, "Heal", "Heals you based on your intelligence and strength"), //
	PRECISE_STRIKE(1, "3", SpecialActionType.SPECIAL_ATTACK, 0, 0, 0, "Precise strike",
			"Physical attack that has higher chance of hitting a weak spot"), //

	BASH(2, "3", SpecialActionType.SPECIAL_ATTACK, 0, 0, 0, "Bash",
			"Powerful physical attack that stuns the enemy and deals increased damage"), //
	DOUBLE_STRIKE(2, "4", SpecialActionType.SPECIAL_ATTACK, 0, 0, 0, "Double strike",
			"Fast physical attack that strikes twice"), //
	FIERY_WEAPON(2, "1", SpecialActionType.SPELL, 0, 0, 0, "Fiery weapon",
			"Covers your weapon in flames, adding magic damage on hit"), //
	FIREBALL(2, "3", SpecialActionType.SPELL, 0, 0, 1, "Fireball",
			"Throws fireball at your enemy, deals more damage to cold/water-based enemies"), //
	FROST_ARMOR(2, "7", SpecialActionType.SPELL, 0, 0, 5, "Frost armor",
			"Gives the caster a powerful armor made of ice"), //
	HOLY_AURA(2, "2", SpecialActionType.SPELL, 0, 0, 2, "Holy aura",
			"Gives you Holy Aura, effect length based on intelligence"), //
	ICE_BOLT(2, "3", SpecialActionType.SPELL, 0, 0, 1, "Ice bolt",
			"Throws an ice bolt at your enemy, deals more damage to hot enemies"), //
	LIFE_DRAIN(2, "6", SpecialActionType.SPELL, 0, 0, 4, "Life drain", "Drains life from enemy and heals caster"), //
	LIGHTNING(2, "3", SpecialActionType.SPELL, 0, 0, 1, "Lightning",
			"Strikes enemy with a lightning, deals more damage to water-based enemies"), //
	MAGIC_SHIELD(2, "1", SpecialActionType.SPELL, 0, 0, 0, "Antimagic shield",
			"Creates shield around the user, reducing incoming magic damage"), //
	PARALYZING_THUNDER(2, "5", SpecialActionType.SPELL, 0, 0, 3, "Paralyzing thunder",
			"Summons a thunder to strike your foe, also causing paralyzing effect"), //
	RAGE(2, "2", SpecialActionType.SPELL, 2, 0, 0, "Rage", "Enrages you, longer effect for stronger fighters"), //
	RAIJU_LIGHTNING(2, "5", SpecialActionType.SPELL, 0, 0, 3, "Raiju lightning",
			"Lightning that can paralyze an enemy if it's strong enough"), //
	SLEEP(2, "6", SpecialActionType.SPELL, 0, 0, 4, "Sleep", "Puts enemy to sleep"), //
	SPEED_OF_WIND(2, "2", SpecialActionType.SPELL, 0, 2, 0, "Speed of wind",
			"Makes you lighter and faster, effect is longer for agile fighters"), //
	STONE_SKIN(2, "5", SpecialActionType.SPELL, 0, 0, 3, "Stone skin", "Hardens your skin, giving you armor"), //

	BLIZZARD(3, "12", SpecialActionType.SPELL, 0, 0, 10, "Blizzard", "Summons a hailing blizzard to strike your foe"), //
	FREEZE(3, "10", SpecialActionType.SPELL, 0, 0, 8, "Freeze", "Freezes the enemy, deals damage to fire enemies"), //
	METEORITE(3, "15", SpecialActionType.SPELL, 0, 0, 13, "Meteorite",
			"Summons a huge rock to fall on your enemy, deals massive damage"), //
	PETRIFY(3, "9", SpecialActionType.SPELL, 0, 0, 7, "Petrify", "Petrifies target"), //
	QUADRUPLE_FLAME(3, "16", SpecialActionType.SPELL, 0, 0, 14, "Quadruple flame",
			"Deals massive fire damage to the target in 4 strikes"), //
	SHADOW_CLONE(3, "5", SpecialActionType.SPECIAL_ACTION, 0, 5, 5, "Shadow clone",
			"Creates a shadow clone, protecting you from hits"), //
	WHIRLPOOL(3, "9", SpecialActionType.SPELL, 0, 0, 7, "Whirlpool",
			"Creates a whirlpool that swings the target around");

	public static enum SpecialActionType {
		SPECIAL_ACTION("special action"), //
		SPECIAL_ATTACK("special attack"), //
		SPELL("spell");

		public final String name;

		private SpecialActionType(final String name) {
			this.name = name;
		}
	}

	public final int tier;
	public final String manaUse;
	public final SpecialActionType type;
	public final RPGFightAction action;

	public final int requiredStrength;
	public final int requiredAgility;
	public final int requiredIntelligence;

	public final String name;
	public final String description;

	private ActiveSkill(final int tier, final String manaUse, final SpecialActionType type, final int requiredStrength,
			final int requiredAgility, final int requiredIntelligence, final String name, String description) {
		this.tier = tier;
		this.manaUse = manaUse;
		this.type = type;
		action = RPGFightAction.valueOf(type.name() + "_" + name());

		this.requiredStrength = requiredStrength;
		this.requiredAgility = requiredAgility;
		this.requiredIntelligence = requiredIntelligence;

		this.name = name;
		final List<String> requirements = new ArrayList<>();
		if (requiredStrength > 0) {
			requirements.add(requiredStrength + " strength");
		}
		if (requiredAgility > 0) {
			requirements.add(requiredAgility + " agility");
		}
		if (requiredIntelligence > 0) {
			requirements.add(requiredIntelligence + " intelligence");
		}
		if (!requirements.isEmpty()) {
			description += "\nRequires " + joinNames(requirements) + " to use";
		}
		this.description = description;

	}

	public String getFullName() {
		return name + " (" + manaUse + " mana)";
	}
}
