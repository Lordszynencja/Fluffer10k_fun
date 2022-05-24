package bot.commands.rpg.skills;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public enum Skill {
	////////////////
	// POWER TREE //
	////////////////
	POWER_TREE("Power tree", "Contains skills for a strong and tough fighter"), //

	POWER_1(2, "Power I", "Increases physical attack damage by 1", POWER_TREE), //
	RAGE(3, "Rage", "Unlocks the >rage< special attack", POWER_1), //
	POWER_2(3, "Power II", "Increases physical attack damage by 2", POWER_1), //
	POWER_3(4, "Power III", "Increases physical attack damage by 3", POWER_2), //
	POWER_4(5, "Power IV", "Increases physical attack damage by 4", POWER_3), //
	POWER_5(10, "Power V", "Increases physical attack damage by 10", POWER_4), //

	THICK_SKIN_1(2, "Thick skin I", "Increases armor and magic resistance by 1", POWER_TREE), //
	THICK_SKIN_2(3, "Thick skin II", "Increases armor and magic resistance by 1 and allows you to wear medium armor",
			THICK_SKIN_1), //
	THICK_SKIN_3(4, "Thick skin III", "Increases armor and magic resistance by 1", THICK_SKIN_2), //
	THICK_SKIN_4(5, "Thick skin IV", "Increases armor and magic resistance by 1 and lets you wear the heavy armor",
			THICK_SKIN_3), //
	THICK_SKIN_5(7, "Thick skin V", "Increases armor and magic resistance by 2", THICK_SKIN_4), //

	BODYBUILDING_1(2, "Bodybuilding I", "Increases strength by 1 and hp by 5", POWER_TREE), //
	BASH(4, "Bash", "Unlocks the >bash< special attack", BODYBUILDING_1), //
	BODYBUILDING_2(3, "Bodybuilding II", "Increases strength by 1 and hp by 7", BODYBUILDING_1), //
	BODYBUILDING_3(4, "Bodybuilding III", "Increases strength by 1 and hp by 10", BODYBUILDING_2), //
	BODYBUILDING_4(5, "Bodybuilding IV", "Increases strength by 1 and hp by 15", BODYBUILDING_3), //
	BODYBUILDING_5(10, "Bodybuilding V", "Increases strength by 5 and hp by 25", BODYBUILDING_4), //

	////////////////////
	// SWIFTNESS TREE //
	////////////////////
	SWIFTNESS_TREE("Swiftness tree", "Contains skills and abilities of a quick and deadly fighter"), //

	SURE_HIT_1(1, "Sure hit I", "Gives +5% hit chance", SWIFTNESS_TREE), //
	SURE_HIT_2(2, "Sure hit II", "Gives +5% hit chance", SURE_HIT_1), //
	DOUBLE_STRIKE(5, "Double strike", "Unlocks the >double strike< special attack", SURE_HIT_2), //
	SURE_HIT_3(3, "Sure hit III", "Gives +5% hit chance", SURE_HIT_2), //
	DUAL_WIELD(5, "Dual wield", "Allows you to wield two single handed weapons at once", SURE_HIT_3), //
	SURE_HIT_4(4, "Sure hit IV", "Gives +5% hit chance", SURE_HIT_3), //
	SURE_HIT_5(5, "Sure hit V", "Gives +5% hit chance", SURE_HIT_4), //

	PRECISE_HIT_1(1, "Precise hit I", "Gives +1 agility and +1% crit chance", SWIFTNESS_TREE), //
	GOUGE(1, "Gouge", "Unlocks the >gouge< special attack", PRECISE_HIT_1), //
	PRECISE_HIT_2(2, "Precise hit II", "Gives +1 agility and +2% crit chance", PRECISE_HIT_1), //
	PRECISE_STRIKE(3, "Precise strike", "Unlocks the >precise strike< special attack", PRECISE_HIT_2), //
	PRECISE_HIT_3(3, "Precise hit III", "Gives +1 agility and +3% crit chance", PRECISE_HIT_2), //
	PRECISE_HIT_4(4, "Precise hit IV", "Gives +1 agility and +3% crit chance", PRECISE_HIT_3), //
	PRECISE_HIT_5(10, "Precise hit V",
			"Gives +5 agility and +10% crit chance, your critical strikes deal 10 bonus damage", PRECISE_HIT_4), //

	ARTFUL_DODGER_1(2, "Artful dodger I", "Gives +3% dodge chance", SWIFTNESS_TREE), //
	SPEED_OF_WIND(3, "Speed of wind", "Unlocks the >speed of wind< spell", ARTFUL_DODGER_1), //
	ARTFUL_DODGER_2(3, "Artful dodger II", "Gives +4% dodge chance", ARTFUL_DODGER_1), //
	ARTFUL_DODGER_3(4, "Artful dodger III", "Gives +5% dodge chance", ARTFUL_DODGER_2), //
	ARTFUL_DODGER_4(5, "Artful dodger IV", "Gives +6% dodge chance", ARTFUL_DODGER_3), //
	ARTFUL_DODGER_5(6, "Artful dodger V", "Gives +7% dodge chance", ARTFUL_DODGER_4), //
	SHADOW_CLONE(5, "Shadow clone", "Unlocks the >shadow clone< active skill", ARTFUL_DODGER_5), //

	///////////////
	// MIND TREE //
	///////////////
	MIND_TREE("Mind tree", "Contains spells and skills increasing magic power"),

	FORCE_HIT(1, "Force hit", "Unlocks the >force hit< spell", MIND_TREE), //
	FIREBALL(2, "Fireball", "Unlocks the >fireball< spell", FORCE_HIT), //
	FIERY_WEAPON(3, "Fiery weapon", "Unlocks the >fiery weapon< spell", FIREBALL), //
	METEORITE(8, "Meteorite", "Unlocks the >meteorite< spell", FIREBALL), //
	ICE_BOLT(2, "Ice bolt", "Unlocks the >ice bolt< spell", FORCE_HIT), //
	FREEZE(5, "Freeze", "Unlocks the >freeze< spell", ICE_BOLT), //
	BLIZZARD(7, "Blizzard", "Unlocks the >blizzard< spell", ICE_BOLT), //
	LIGHTNING(2, "Lightning", "Unlocks the >lightning< spell", FORCE_HIT), //
	WHIRLPOOL(5, "Whirlpool", "Unlocks the >whirlpool< spell", FORCE_HIT), //
	SLEEP(2, "Sleep", "Unlocks the >sleep< spell", FORCE_HIT), //

	MAGIC_POWER_1(2, "Magic power I", "Increases intelligence by 1 and magic power by 2", MIND_TREE), //
	MAGIC_POWER_2(3, "Magic power II", "Increases intelligence by 1 and magic power by 3", MAGIC_POWER_1), //
	MAGIC_POWER_3(4, "Magic power III", "Increases intelligence by 1 and magic power by 4", MAGIC_POWER_2), //
	MAGIC_POWER_4(5, "Magic power IV", "Increases intelligence by 1 and magic power by 7", MAGIC_POWER_3), //
	MAGIC_POWER_5(10, "Magic power V", "Increases intelligence by 5 and magic power by 15", MAGIC_POWER_4), //

	MANA_MASTER_1(1, "Mana master I", "Increases max mana by 2", MIND_TREE), //
	MANA_MASTER_2(1, "Mana master II", "Increases max mana by 4", MANA_MASTER_1), //
	MANA_MASTER_3(2, "Mana master III", "Increases max mana by 6", MANA_MASTER_2), //
	MANA_MASTER_4(2, "Mana master IV", "Increases max mana by 8", MANA_MASTER_3), //
	MANA_MASTER_5(4, "Mana master V", "Increases max mana by 20%", MANA_MASTER_4), //
	MANA_ARCHMASTER(5, "Mana archmaster", "Increases magic power by 1 for every 10 mana in your pool", MANA_MASTER_5), //

	STRONG_MIND(2, "Strong mind", "Reduces charm time by 2 turns", MIND_TREE), //
	STATUS_RESISTANCE_1(3, "Status resistance I", "Reduces negative statuses duration by 1 turn", STRONG_MIND), //
	STATUS_RESISTANCE_2(5, "Status resistance II", "Further reduces negative statuses duration by 1 turn",
			STATUS_RESISTANCE_1), //

	////////////////////////
	// SURVIVABILITY TREE //
	////////////////////////
	SURVIVABILITY_TREE("Survivability tree", "Contains skills that help the fighter survive for longer"), //

	AGILE_FIGHTER_1(1, "Agile Fighter I", "Increases dodge chance by 2%", SURVIVABILITY_TREE), //
	AGILE_FIGHTER_2(2, "Agile Fighter II", "Increases dodge chance by 3%", AGILE_FIGHTER_1), //
	AGILE_FIGHTER_3(3, "Agile Fighter III", "Increases dodge chance by 4%", AGILE_FIGHTER_2), //
	AGILE_FIGHTER_4(4, "Agile Fighter IV", "Increases dodge chance by 6%", AGILE_FIGHTER_3), //
	AGILE_FIGHTER_5(5, "Agile Fighter V", "Increases dodge chance by 10%", AGILE_FIGHTER_4), //

	TOUGHNESS_1(1, "Toughness I", "Increases your max HP by 3", SURVIVABILITY_TREE), //
	TOUGHNESS_2(2, "Toughness II", "Increases your max HP by 5", TOUGHNESS_1), //
	TOUGHNESS_3(3, "Toughness III", "Increases your max HP by 7", TOUGHNESS_2), //
	TOUGHNESS_4(4, "Toughness IV", "Increases your max HP by 9", TOUGHNESS_3), //
	TOUGHNESS_5(5, "Toughness V", "Increases your max HP by 20%", TOUGHNESS_4), //

	HEAL(1, "Heal", "Unlocks the >heal< spell", SURVIVABILITY_TREE), //
	STONE_SKIN(3, "Stone Skin", "Unlocks the >stone skin< spell", HEAL), //
	ANTIMAGIC_SHIELD(3, "Antimagic shield", "Unlocks the >antimagic shield< spell", HEAL), //
	INNATE_RESISTANCE(5, "Innate resistance", "You get 1 armor and magic resistance", STONE_SKIN, ANTIMAGIC_SHIELD), //
	HOLY_AURA(2, "Holy aura", "Unlocks the >holy aura< spell", HEAL), //
	FAST_HEALING_1(5, "Fast healing I", "Gives you 1 HP regen per turn", HEAL), //
	FAST_HEALING_2(7, "Fast healing II", "Gives you 1 HP regen per turn", FAST_HEALING_1), //
	FAST_HEALING_3(10, "Fast healing III", "Spells and potions heal you 2x more", FAST_HEALING_2);

	public static List<Skill> trees = asList(POWER_TREE, SWIFTNESS_TREE, MIND_TREE, SURVIVABILITY_TREE);

	public final int price;
	public final String name;
	public final String description;

	public final List<Skill> sub = new ArrayList<>();
	public final List<Skill> parents;

	private static int getTotalPriceOfTree(final Skill tree) {
		int total = 0;
		final List<Skill> skillsLeft = new ArrayList<>();
		skillsLeft.add(tree);
		while (!skillsLeft.isEmpty()) {
			final Skill skill = skillsLeft.remove(0);
			total += skill.price;
			skillsLeft.addAll(skill.sub);
		}
		return total;
	}

	static {
		for (final Skill tree : trees) {
			@SuppressWarnings("unused")
			final int total = getTotalPriceOfTree(tree);
			// System.out.println("total price of " + tree.name + " tree: " + total);
		}
	}

	private Skill(final String name, final String description) {
		this(0, name, description);
	}

	private Skill(final int price, final String name, final String description, final Skill... parents) {
		this.price = price;
		this.name = name;
		this.description = description;

		if (parents.length == 0) {
			this.parents = new ArrayList<>();
			this.parents.add(null);
		} else {
			this.parents = asList(parents);
			this.parents.forEach(parent -> parent.sub.add(this));
		}
	}
}
