package bot.commands.rpg;

import static bot.userData.UserBlessingData.Blessing.CHANNELING;
import static bot.userData.UserBlessingData.Blessing.DECISIVE_STRIKE;
import static bot.userData.UserBlessingData.Blessing.DWARVEN_ANCESTORS;
import static bot.userData.UserBlessingData.Blessing.ENTHUSIASTIC_FIGHTER;
import static bot.userData.UserBlessingData.Blessing.FAMILY_MAN;
import static bot.userData.UserBlessingData.Blessing.FAST_RUNNER;
import static bot.userData.UserBlessingData.Blessing.GEM_SPECIALIST;
import static bot.userData.UserBlessingData.Blessing.LAST_STAND;
import static bot.userData.UserBlessingData.Blessing.LUCKY_DUCKY;
import static bot.userData.UserBlessingData.Blessing.MANA_MANIAC;
import static bot.userData.UserBlessingData.Blessing.MARTIAL_ARTIST;
import static bot.userData.UserBlessingData.Blessing.MARTYRDOM;
import static bot.userData.UserBlessingData.Blessing.NATURAL_CHARM;
import static bot.userData.UserBlessingData.Blessing.NATURAL_RESISTANCE;
import static bot.userData.UserBlessingData.Blessing.POTION_MASTER;
import static bot.userData.UserBlessingData.Blessing.QUICK_RECOVERY;
import static bot.userData.UserBlessingData.Blessing.TERRIFYING_PRESENCE;
import static bot.userData.UserBlessingData.Blessing.THE_STORM_THAT_IS_APPROACHING;
import static bot.userData.UserBlessingData.Blessing.TRADER;
import static bot.userData.UserBlessingData.Blessing.UNDYING;
import static bot.userData.UserBlessingData.Blessing.UNSATISFIED_EXPLORER;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;
import static bot.util.modularPrompt.ModularPromptButton.button;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandBlessings extends Command {
	private static final Map<Blessing, String> blessingDescriptions = toMap(//
			pair(CHANNELING, "Every time you recast a spell that you casted previous turn, it has +1 to dmg/duration."), //
			pair(DECISIVE_STRIKE,
					"Whenever you critically strike, you get +1 dmg for next attack (stacks up to 2 times)."), //
			pair(DWARVEN_ANCESTORS, "You have a 25% chance to find ore vein, which gives lots of ore."), //
			pair(ENTHUSIASTIC_FIGHTER, "You are quick, giving you first turn for all enemies that aren't also quick."), //
			pair(FAMILY_MAN, "10% higher chance to recruit a girl to your harem, for a total of 20%."), //
			pair(FAST_RUNNER, "You can escape the fight."), //
			pair(GEM_SPECIALIST, "During mining, you always find bonus gem."), //
			pair(LAST_STAND, "You get +1 to all stats when your hp is below 20%."), //
			pair(LUCKY_DUCKY, "Get 50% more gold from battles."), //
			pair(MANA_MANIAC, "20% chance for a spell to not use any mana."), //
			pair(MARTIAL_ARTIST, "You deal increased damage based on strength when not holding any weapon."), //
			pair(MARTYRDOM, "During battle, if you lose while enemy has less than 25% hp, you don't lose gold."), //
			pair(NATURAL_CHARM,
					"When monster girl tries to charm you, it has a 20% chance to backfire and charm her instead."), //
			pair(NATURAL_RESISTANCE, "You get 3 base armor."), //
			pair(POTION_MASTER, "Health potions and mana potions regenerate +2 base and +20% total."), //
			pair(QUICK_RECOVERY,
					"Fights use less stamina based on HP% you have left after fight (reduce by 25 at full HP, 0 at 25% HP and less)."), //
			pair(TERRIFYING_PRESENCE,
					"If enemy is half your level or lower, they have a chance to flee and leave the reward for you, and you lose only 5 stamina."), //
			pair(THE_STORM_THAT_IS_APPROACHING, "Higher chance to encounter stronger enemies."), //
			pair(TRADER, "You sell items for 10% more gold."), //
			pair(UNDYING, "You are left with 1 hp if an attack would defeat you, once per battle."), //
			pair(UNSATISFIED_EXPLORER, "When nothing is found during exploration, you lose no stamina."));

	private final Fluffer10kFun fluffer10kFun;

	public CommandBlessings(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "blessings", "List the blessings", false);

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField blessingToField(final ServerUserData userData, final Blessing blessing) {
		final String name = blessing.name
				+ (userData.blessings.blessingsObtained.contains(blessing) ? " (obtained)" : "");
		final String value = blessingDescriptions.get(blessing);
		return new EmbedField(name, value);
	}

	private PagedMessage makeList(final int page, final ServerUserData userData) {
		final List<Blessing> blessings = Blessing.list;
		final List<EmbedField> fields = mapToList(blessings, blessing -> blessingToField(userData, blessing));

		final int blessingsLeft = userData.blessings.blessings - userData.blessings.blessingsObtained.size();
		final String description = blessingsLeft > 0 ? "blessings left to choose: " + blessingsLeft : null;

		return new PagedPickerMessageBuilder<>("Blessings", 7, fields, blessings)//
				.description(description)//
				.onPick((in, page2, blessing) -> sendDetails(in, page2, userData, blessing))//
				.page(page)//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(0, userData), interaction);
	}

	private void sendDetails(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Blessing blessing) {
		final boolean obtained = userData.blessings.blessingsObtained.contains(blessing);

		final String title = blessing.name + (obtained ? " (obtained)" : "");
		final String description = blessingDescriptions.get(blessing);
		final EmbedBuilder embed = makeEmbed(title, description);
		final boolean obtainDisabled = obtained
				|| userData.blessings.blessings <= userData.blessings.blessingsObtained.size();

		final ModularPrompt prompt = new ModularPrompt(embed, //
				button("Obtain", ButtonStyle.SUCCESS, in2 -> obtainBlessing(in2, page, userData, blessing),
						obtainDisabled), //
				button("Back", ButtonStyle.DANGER, in2 -> onBack(in2, page, userData)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, in);
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(page, userData), in);
	}

	private void obtainBlessing(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Blessing blessing) {
		if (userData.blessings.blessings > userData.blessings.blessingsObtained.size()) {
			userData.blessings.blessingsObtained.add(blessing);
		}

		sendDetails(in, page, userData, blessing);
	}
}
