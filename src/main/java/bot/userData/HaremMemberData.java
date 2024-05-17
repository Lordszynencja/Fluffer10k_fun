package bot.userData;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.Utils.bold;
import static bot.util.Utils.doubleFromNumber;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import bot.data.MonsterGirls;
import bot.data.MonsterGirls.MonsterGirlRace;

public class HaremMemberData implements Comparable<HaremMemberData> {
	public static final String[] defaultNames = { //
			"Alice", "Anna", "Aracely", //
			"Barbara", //
			"Cathyl", //
			"Elisa", "Ernestine", //
			"Fukuda", //
			"Heidi", "Hila", //
			"Irina", "Irma", "Isabela", //
			"Janice", "Jeniffer", "Jeshna", //
			"Katherine", "Kuro", //
			"Liera", "Lilian", "Lisa", //
			"Mari", "Maria", "Meru", "Momo", "Monika", //
			"Nancy", "Noelle", //
			"Patricia", //
			"Rose", "Rubi", //
			"Shiro", "Shun", "Suzu", //
			"Tallulah", "Ticy", //
			"Vanessa", //
			"Zonda" };

	public enum HaremMemberInteraction {
		CAFE("meet with %1$s at a cafe"), //
		CINEMA("go to the cinema with %1$s"), //
		COOK("cook something for %1$s"), //
		CUDDLE("cuddle %1$s"), //
		DANCE("dance with %1$s"), //
		DATE("go on a date with %1$s"), //
		EAT_TOGETHER("eat together with %1$s"), //
		FEED("feed %1$s"), //
		FLOWERS("give %1$s flowers"), //
		FUCK("fuck %1$s"), //
		FUCK_LIKE_CRAZY("fuck %1$s like crazy"), //
		HUG("hug %1$s"), //
		KISS("kiss %1$s"), //
		MOVIE("watch a movie with %1$s"), //
		PAT("pat %1$s"), //
		RESTAURANT("go to a fancy restaurant with %1$s"), //
		SEX("have sex with %1$s"), //
		SLEEP_LONGER("sleep a few more minutes with %1$s"), //
		SNUGGLE("snuggle %1$s");

		public final String interactionFormat;

		private HaremMemberInteraction(final String interactionFormat) {
			this.interactionFormat = interactionFormat;
		}
	}

	public String name;
	public final MonsterGirlRace race;
	public double affection;
	public HaremMemberInteraction desiredInteraction;
	public boolean married;
	public boolean changeable;

	public HaremMemberData(final String name, final MonsterGirlRace race) {
		this.name = name;
		this.race = race;
		affection = getRandomInt(10, 90);
		desiredInteraction = getRandom(HaremMemberInteraction.values());
		married = false;
		changeable = true;
	}

	public HaremMemberData(final Map<String, Object> data, final String name) {
		this.name = name;
		final String raceStr = (String) data.get("race");
		MonsterGirlRace actualRace;
		try {
			actualRace = MonsterGirlRace.valueOf(raceStr);
		} catch (final Exception e) {
			actualRace = MonsterGirls.getByName(raceStr);
		}
		race = actualRace;
		affection = doubleFromNumber(data.getOrDefault("affection", 0));
		desiredInteraction = data.get("desiredInteraction") == null ? null
				: HaremMemberInteraction.valueOf((String) data.get("desiredInteraction"));
		married = (boolean) data.get("married");
		changeable = (boolean) data.getOrDefault("changeable", true);
	}

	public Map<String, Object> toData() {
		final Map<String, Object> map = new HashMap<>();

		map.put("name", name);
		map.put("race", race.name());
		map.put("affection", affection);
		map.put("desiredInteraction", desiredInteraction);
		map.put("married", married);
		map.put("changeable", changeable);

		return map;
	}

	public int getMaxAffection() {
		return married ? 200 : 100;
	}

	public void addAffection(final double bonus) {
		affection += bonus;
		if (affection > getMaxAffection()) {
			affection = getMaxAffection();
		}
	}

	@Override
	public String toString() {
		return "{name: " + name + ", race: " + race + ", affection: " + affection + "}";
	}

	public String getFullName() {
		return race.race + " " + name + getMarriedString();
	}

	public String getFullNameWithTypeFamily() {
		return race.race + " " + name + " (" + race.type + " type - " + race.family + " family)";
	}

	public String getAffectionDescription() {
		if (affection <= 0) {
			return "furiously mad at you!!!";
		}
		if (affection <= 10) {
			return "really angry!!";
		}
		if (affection <= 25) {
			return "angry!";
		}
		if (affection <= 40) {
			return "upset";
		}
		if (affection <= 60) {
			return "normal";
		}
		if (affection <= 75) {
			return "happy";
		}
		if (affection <= 90) {
			return "charmed";
		}
		if (affection <= 150) {
			return "in love";
		}
		return "always with you, whenever she can";
	}

	public String getDescription() {
		String description = "Affection: " + getAffectionDescription() + " (" + affection + ")";
		if (desiredInteraction != null) {
			description += "\nShe wants you to " + bold(String.format(desiredInteraction.interactionFormat, "her"))
					+ " today";
		}
		return description;
	}

	public String getFormattedInteraction() {
		return String.format(desiredInteraction.interactionFormat, name);
	}

	public boolean checkIfInteractionIsValid(final String interaction) {
		return desiredInteraction != null && getFormattedInteraction().equals(interaction);
	}

	public String getMarriedString() {
		return married ? " 💍" : "";
	}

	public EmbedBuilder toEmbed() {
		return makeEmbed(getFullNameWithTypeFamily() + getMarriedString(), getDescription(), race.imageLink);
	}

	@Override
	public int compareTo(final HaremMemberData o) {
		return name.compareTo(o.name);
	}

	public boolean canBeMarried() {
		return !married && affection > 90;
	}
}
