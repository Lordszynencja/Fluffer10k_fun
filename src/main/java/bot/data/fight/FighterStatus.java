package bot.data.fight;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.fixString;

import java.util.Map;

public enum FighterStatus {
	AGILITY_1(new FighterStatusBuilder("Minor agility buff", //
			"Adds 1 to agility until the fight ends, doesn't stack", //
			"Minor agility buff vanishes from %1$s!")//
			.agility(1)), //
	AGILITY_2(new FighterStatusBuilder("Agility buff", //
			"Adds 3 to agility until the fight ends, doesn't stack", //
			"Agility buff vanishes from %1$s!")//
			.agility(3)), //
	AGILITY_3(new FighterStatusBuilder("Major agility buff", //
			"Adds 5 to agility until the fight ends, doesn't stack", //
			"Major agility buff vanishes from %1$s!")//
			.agility(5)), //
	ALRAUNE_VINES(new FighterStatusBuilder("Wrapped in Alraune's vines", //
			"Reduces your statistics by one for each stack while you stay entangled", //
			"%1$s gets free from Alraune's vines!")//
			.preventsItemUse()//
			.canBreakFree()), //
	ATLACH_NACHA_VENOM(new FighterStatusBuilder("Atlach-Nacha venom", //
			"Reduces all stats by 1 for every other stack", //
			"Atlach-Nacha venom stops poisoning %1$s's blood!")), //
	BANSHEE_WAIL(new FighterStatusBuilder("Banshee's wail", //
			"Gives penalty of -1 strength, -1 agility and -3 intelligence", //
			"effect of banshee's song fades away from %1$s!")//
			.strength(-1).agility(-1).intelligence(-3)//
			.negativeExpiringFaster()), //
	BAROMETZ_COTTON(new FighterStatusBuilder("Barometz's scented cotton", //
			"Reduces willpower, -3 intelligence", //
			"Effect of barometz's scented cotton fades away from %1$s!")//
			.intelligence(-3)//
			.negativeExpiringFaster()), //
	BASILISK_EYES(new FighterStatusBuilder("Basilisk eyes", //
			"Reduces all stats by 10", //
			"%1$s can move freely again!")//
			.strength(-10).agility(-10).intelligence(-10)//
			.preventsActions()//
			.preventsDodge()//
			.negativeExpiringFaster()), //
	BLEEDING(new FighterStatusBuilder("Bleeding", //
			"Causes fighter to take damage every turn", //
			"%1$s is not bleeding anymore!")), //
	BUBBLES(new FighterStatusBuilder("Bubbles", //
			"Fighter releases bubbles to increase intelligence by 3", //
			"%1$s is no longer releasing bubbles!")//
			.intelligence(3)), //
	CHARM_RESISTANCE(new FighterStatusBuilder("Charm resistance", //
			"Fighter can resist charms", //
			"The charm resistance disappears from %1$s!")), //
	CHARMED(new FighterStatusBuilder("Charmed", //
			"Fighter is charmed, reducing all stats by 2", //
			"The charm fades away from %1$s!")//
			.strength(-2).agility(-2).intelligence(-2)//
			.preventsActions()//
			.negativeExpiringFaster()), //
	CURSED(new FighterStatusBuilder("Cursed", //
			"Fighter is cursed, 50% of his attacks miss and his spells have -4 damage roll penalty", //
			"The curse fades away from %1$s!")//
			.negativeExpiringFaster()), //
	CURSED_SWORD_CUT(new FighterStatusBuilder("Cursed Sword cut", //
			"Fighter is cut by Cursed Sword, reduces stats by 1 for every two stacks", //
			"Cursed Sword cuts heal on %1$s!")), //
	DECISIVE_STRIKE(new FighterStatusBuilder("Decisive strike", //
			"Fighter deals more damage because of last attack being a crit", //
			"%1$s's decisive strike is gone!")), //
	DOPPELGANGER(new FighterStatusBuilder("Doppelganger", //
			"Fighter has a doppelganger, doubling their action count!", //
			"%1$s's doppelganger vanishes!")), //
	DORMOUSE_SLEEP(new FighterStatusBuilder("Sleep", //
			"Enemy sleeps, waiting for player to attack", //
			"%1$s wakes up!")//
			.preventsDodge()), //
	DRUNK(new FighterStatusBuilder("Drunk", //
			"Enemy is drunk, increasing strength by 3, but getting -3 to agility and -3 to intelligence", //
			"%1$s is no longer drunk!")//
			.strength(3).agility(-3).intelligence(-3)), //
	EGG_SHELL(new FighterStatusBuilder("Egg shell", //
			"Increases armor by 2", //
			"%1$s is no longer protected by shell!")//
			.armor(2)), //
	ENRAGED(new FighterStatusBuilder("Enraged", //
			"Increases your damage by 25%, but makes you unable to hit critically", //
			"%1$s's rage ends!")), //
	FIERY_WEAPON(new FighterStatusBuilder("Fiery weapon", //
			"fighter's weapon is engulfed in flames, increasing damage", //
			"%1$s's weapon is no longer covered in flames!")), //
	FREEZING(new FighterStatusBuilder("Freezing", //
			"Reduces all stats by 1 for every stack", //
			"%1$s's warms up!")), //
	FRENZY(new FighterStatusBuilder("Frenzy", //
			"fighter goes into frenzy, increasing strength by 2 and agility by 2", //
			"%1$s is no longer in frenzy!")//
			.strength(2).agility(2)), //
	FROST_ARMOR(new FighterStatusBuilder("Frost armor", //
			"Increases armor by 4", //
			"%1$s's frost armor breaks!")//
			.armor(4)), //
	FROZEN(new FighterStatusBuilder("Frozen", //
			"Fighter is frozen", //
			"%1$s is no longer frozen!")//
			.preventsActions()//
			.preventsDodge()), //
	GROWN(new FighterStatusBuilder("Grown", //
			"Fighter is enlarged, +3 strength, -3 agility", //
			"%1$s is no longer big!")//
			.strength(3).agility(-3)), //
	HELD(new FighterStatusBuilder("Held down", //
			"Fighter is held in place, -2 strength and -4 agility", //
			"%1$s breaks free!")//
			.strength(-2).agility(-4)//
			.preventsItemUse()//
			.canBreakFree()), //
	HOLY_AURA(new FighterStatusBuilder("Holy Aura", //
			"Adds 1 armor and magic resistance, heals 1 HP per turn and deals 1 damage per turn to undead enemies", //
			"%1$s's holy aura fades away!")//
			.armor(1).magicResistance(1)//
			.healthRegen(1)), //
	ILLUSION_WORLD(new FighterStatusBuilder("Illusion world", //
			"Reduces your stats by one for first stack and every two stacks after first, and causes you to be defeated if there are more stacks than your sum of stats", //
			"%1$s breaks out of the illusion!")), //
	IN_SMOKE(new FighterStatusBuilder("In smoke", //
			"Makes you miss attacks", //
			"smoke disperses around %1$s!")//
			.preventsDodge()), //
	INTELLIGENCE_1(new FighterStatusBuilder("Minor intelligence buff", //
			"Adds 1 to intelligence until the fight ends, doesn't stack", //
			"Minor intelligence buff vanishes from %1$s!")//
			.intelligence(1)), //
	INTELLIGENCE_2(new FighterStatusBuilder("Intelligence buff", //
			"Adds 3 to intelligence until the fight ends, doesn't stack", //
			"Intelligence buff vanishes from %1$s!")//
			.intelligence(3)), //
	INTELLIGENCE_3(new FighterStatusBuilder("Major intelligence buff", //
			"Adds 5 to intelligence until the fight ends, doesn't stack", //
			"Major intelligence buff vanishes from %1$s!")//
			.intelligence(5)), //
	INTOXICATED(new FighterStatusBuilder("Intoxicated", //
			"Gives a penalty of -2 to strength, -4 to agility and -2 to intelligence", //
			"%1$s is no longer intoxicated!")//
			.strength(-2).agility(-4).intelligence(-2)//
			.negativeExpiringFaster()), //
	KEJOUROU_HAIR(new FighterStatusBuilder("Wrapped in Kejourou's hair", //
			"Reduces your statistics by one for each other stack while you stay entangled", //
			"%1$s gets free from Kejorou's hair!")//
			.preventsItemUse()//
			.canBreakFree()), //
	LAVA_GOLEM_TEMPERATURE(new FighterStatusBuilder("Lava Golem temperature", //
			"Lava golem can change temperature of their body to increase speed or armor", //
			"%1$s dried up a bit!")//
			.agility(2)//
			.armor(-2)//
			.statsStack()), //
	LICKED(new FighterStatusBuilder("Licked", //
			"Fighter is covered in slimy mucus, giving penalty of -3 agility", //
			"%1$s dried up a bit!")//
			.agility(-3)//
			.negativeExpiringFaster()), //
	MAGIC_SHIELD(new FighterStatusBuilder("Magic shield", //
			"Protects the fighter, reducing incoming magic damage based on intelligence", //
			"%1$s is no longer protected by magic shield!")), //
	MOTHMAN_POWDER(new FighterStatusBuilder("Mothman powder", //
			"Increases charm duration", //
			"%1$s is no longer covered in Mothman powder!")), //
	MUMMY_CURSE(new FighterStatusBuilder("Mummy curse", //
			"Doubles the damage to you on each successful attack", //
			"%1$s is no longer under Mummy Curse!")//
			.negativeExpiringFaster()), //
	NEUROTOXIN(new FighterStatusBuilder("Neurotoxin", //
			"Reduces all stats by 1 for every other stack", //
			"Neurotoxin stops poisoning %1$s's blood!")), //
	OCELOMEH_SWORD(new FighterStatusBuilder("Ocelomeh's sword effect", //
			"Gives penalty of -2 to all stats", //
			"%1$s is no longer under the effect of Ocelomeh's sword!")//
			.strength(-2).agility(-2).intelligence(-2)//
			.negativeExpiringFaster()), //
	PARALYZED(new FighterStatusBuilder("Paralyzed", //
			"Fighter is paralyzed", //
			"%1$s is no longer paralyzed!")//
			.preventsActions()//
			.preventsDodge()//
			.negativeExpiringFaster()), //
	PETRIFIED(new FighterStatusBuilder("Petrified", //
			"Fighter is partially turned into magical stone", //
			"%1$s is no longer petrified!")//
			.preventsActions()//
			.preventsDodge()//
			.negativeExpiringFaster()), //
	POISON(new FighterStatusBuilder("Poison", //
			"Reduces your stats by one for first stack and every two stacks after first, and deals damage every turn", //
			"%1$s gets the poison out of their body!")), //
	RAIJU_CHARGE(new FighterStatusBuilder("Raiju charge", //
			"Fighter charges a shock", //
			"%1$s loses the charge!")), //
	RESTRICTED(new FighterStatusBuilder("Restricted", //
			"Fighter can't do any actions", //
			"%1$s is no longer restricted!")//
			.preventsActions()//
			.preventsDodge()), //
	SALTED(new FighterStatusBuilder("Salted", //
			"Fighter is salted, some slugs are vulnearable to salt", //
			"%1$s is no longer covered in salt!")//
			.strength(-3)), //
	SANDWORM_SHELL(new FighterStatusBuilder("Sandworm shell", //
			"Sandworm is hidden in her shell, she slowly heals and damage to her is heavily reduced", //
			"%1$s comes out of her shell!")//
			.armor(5)//
			.magicResistance(5)), //
	SHADOW_CLONE(new FighterStatusBuilder("shadow clone", //
			"Fighter has a clone, reducing chance to hit the true form", //
			"%1$s's clone disappears!")), //
	SHRINKED(new FighterStatusBuilder("Shrinked", //
			"Fighter is shrinked, -3 strength, +3 agility", //
			"%1$s goes back to normal size!")//
			.strength(-3).agility(3)//
			.negativeExpiringFaster()), //
	SIREN_SONG(new FighterStatusBuilder("Siren song", //
			"Fighter is under the influence of Siren song", //
			"%1$s is no longer influenced by Siren song!")), //
	SLEEP(new FighterStatusBuilder("Sleep", //
			"Fighter is asleep", //
			"%1$s wakes up!")//
			.preventsActions()//
			.preventsDodge()//
			.negativeExpiringFaster()), //
	SLEEP_RESISTANCE(new FighterStatusBuilder("Sleep resistance", //
			"Fighter can resist sleep spells", //
			"The sleep resistance disappears from %1$s!")), //
	SLIME_REGEN(new FighterStatusBuilder("Slime regen", //
			"Fighter is regenerating every turn", //
			"%1$s can't regenerate!")), //
	SPEED_OF_WIND(new FighterStatusBuilder("Speed of Wind", //
			"Adds 2 agility", //
			"%1$s is no longer as fast as the wind!")//
			.agility(2)), //
	SPIRIT_FORM(new FighterStatusBuilder("Spirit form", //
			"Reduces physical damage by half", //
			"%1$s took physical form!")), //
	STONE_SKIN(new FighterStatusBuilder("Stone skin", //
			"Adds 2 armor", //
			"%1$s's skin softens!")//
			.armor(2)), //
	STUNNED(new FighterStatusBuilder("Stunned", //
			"Fighter is stunned, preventing all actions", //
			"%1$s is no longer stunned!")//
			.preventsActions()//
			.preventsDodge()), //
	SUCCUBUS_NOSTRUM(new FighterStatusBuilder("Succubus Nostrum", //
			"Increases intelligence by 3", //
			"%1$s is no longer under the effect of Succubus Nostrum!")//
			.intelligence(3)), //
	STRENGTH_1(new FighterStatusBuilder("Minor strength buff", //
			"Adds 1 to strength until the fight ends, doesn't stack", //
			"Minor strength buff vanishes from %1$s!")//
			.strength(1)), //
	STRENGTH_2(new FighterStatusBuilder("Strength buff", //
			"Adds 3 to strength until the fight ends, doesn't stack", //
			"Strength buff vanishes from %1$s!")//
			.strength(3)), //
	STRENGTH_3(new FighterStatusBuilder("Major strength buff", //
			"Adds 5 to strength until the fight ends, doesn't stack", //
			"Major strength buff vanishes from %1$s!")//
			.strength(5)), //
	UNDYING(new FighterStatusBuilder("Undying", //
			"If you die, you are instead left on 1 hp and this buff gets removed", //
			"%1$s is no longer undying!")), //
	WEAKNESS_FOUND(new FighterStatusBuilder("Weakness found", //
			"Increases all damage to you by 2 for each attack that hits", //
			"%1$s covers the weakness!")), //
	WRAPPED_IN_WEB(new FighterStatusBuilder("Wrapped in web", //
			"Gives penalty of -3 strength and -3 agility", //
			"%1$s gets free from the web!")//
			.strength(-3).agility(-3)//
			.preventsItemUse()//
			.canBreakFree());

	private static class FighterStatusBuilder {
		public final String name;
		public final String description;
		public final String expireMessage;

		public int strength = 0;
		public int agility = 0;
		public int intelligence = 0;
		public int armor = 0;
		public int magicResistance = 0;

		public int healthRegen = 0;

		public boolean preventsActions = false;
		public boolean preventsItemUse = false;
		public boolean preventsDodge = false;
		public boolean negativeExpiringFaster = false;
		public boolean canBreakFree = false;
		public boolean statsStack = false;

		public FighterStatusBuilder(final String name, final String description, final String expireMessage) {
			this.name = name;
			this.description = description;
			this.expireMessage = expireMessage;
		}

		public FighterStatusBuilder strength(final int strength) {
			this.strength = strength;
			return this;
		}

		public FighterStatusBuilder agility(final int agility) {
			this.agility = agility;
			return this;
		}

		public FighterStatusBuilder intelligence(final int intelligence) {
			this.intelligence = intelligence;
			return this;
		}

		public FighterStatusBuilder armor(final int armor) {
			this.armor = armor;
			return this;
		}

		public FighterStatusBuilder magicResistance(final int magicResistance) {
			this.magicResistance = magicResistance;
			return this;
		}

		public FighterStatusBuilder healthRegen(final int healthRegen) {
			this.healthRegen = healthRegen;
			return this;
		}

		public FighterStatusBuilder preventsActions() {
			preventsActions = true;
			preventsItemUse = true;
			return this;
		}

		public FighterStatusBuilder preventsItemUse() {
			preventsItemUse = true;
			return this;
		}

		public FighterStatusBuilder preventsDodge() {
			preventsDodge = true;
			return this;
		}

		public FighterStatusBuilder negativeExpiringFaster() {
			negativeExpiringFaster = true;
			return this;
		}

		public FighterStatusBuilder canBreakFree() {
			canBreakFree = true;
			return this;
		}

		public FighterStatusBuilder statsStack() {
			statsStack = true;
			return this;
		}
	}

	public final String name;
	public final String description;
	public final String expireMessage;

	public final int strength;
	public final int agility;
	public final int intelligence;
	public final int armor;
	public final int magicResistance;

	public final int healthRegen;

	public final boolean preventsActions;
	public final boolean preventsItemUse;
	public final boolean preventsDodge;
	public final boolean negativeExpiringFaster;
	public final boolean canBreakFree;
	public final boolean statsStack;

	private FighterStatus(final FighterStatusBuilder builder) {
		name = builder.name;
		description = builder.description;
		expireMessage = builder.expireMessage;

		strength = builder.strength;
		agility = builder.agility;
		intelligence = builder.intelligence;
		armor = builder.armor;
		magicResistance = builder.magicResistance;

		healthRegen = builder.healthRegen;

		preventsActions = builder.preventsActions;
		preventsItemUse = builder.preventsItemUse;
		preventsDodge = builder.preventsDodge;
		negativeExpiringFaster = builder.negativeExpiringFaster;
		canBreakFree = builder.canBreakFree;
		statsStack = builder.statsStack;
	}

	private static Map<String, FighterStatus> statusesByName = toMap(status -> fixString(status.name),
			FighterStatus.values());

	public static FighterStatus findByName(final String name) {
		return statusesByName.get(fixString(name));
	}
}
