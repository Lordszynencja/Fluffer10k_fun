package bot.commands.rpg.blacksmith;

import static bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList.getBlueprint;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprint;
import bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource;
import bot.commands.rpg.blacksmith.blueprints.utils.ItemPayer;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.data.items.Item;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandBlacksmithBlueprints extends Subcommand {

	private static SlashCommandOption makeTierOption() {
		final List<SlashCommandOptionChoice> choices = asList(BlacksmithTier.values()).stream()//
				.map(tier -> SlashCommandOptionChoice.create(tier.name, tier.name()))//
				.collect(toList());

		return SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "tier", "filter by tier", false,
				choices);
	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandBlacksmithBlueprints(final Fluffer10kFun fluffer10kFun) {
		super("blueprints", "List your blueprints", //
				makeTierOption());

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField blueprintToField(final ServerUserData userData, final BlacksmithBlueprint blueprint) {
		final String name = blueprint.name;
		final String description = "" + userData.blacksmith.blueprints.get(blueprint.id);
		return new EmbedField(name, description);
	}

	private PagedMessage makeList(final int page, final ServerUserData userData, final BlacksmithTier tier) {
		final List<BlacksmithBlueprint> blueprints = userData.blacksmith.blueprints.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> getBlueprint(entry.getKey()))//
				.collect(toList());
		if (tier != null) {
			blueprints.removeIf(blueprint -> blueprint.tier != tier);
		}

		final List<EmbedField> fields = mapToList(blueprints, blueprint -> blueprintToField(userData, blueprint));

		return new PagedPickerMessageBuilder<>("Blueprints", 5, fields, blueprints)//
				.onPick((in, page2, blueprint) -> sendBlueprintDetails(in, page2, userData, blueprint, tier))//
				.page(page)//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (!userData.blacksmith.available) {
			sendEphemeralMessage(interaction, "This feature is not unlocked yet");
			return;
		}

		final String tierParam = getOption(interaction).getArgumentStringValueByName("tier").orElse(null);
		final BlacksmithTier tier = tierParam == null ? null : BlacksmithTier.valueOf(tierParam);

		fluffer10kFun.pagedMessageUtils.addMessage(makeList(0, userData, tier), interaction);
	}

	private EmbedBuilder blueprintDetailsEmbed(final ServerUserData userData, final BlacksmithBlueprint blueprint,
			final String prepend) {
		final String title = blueprint.name;
		final String description = prepend + "Required materials:\n"//
				+ blueprint.resources.description(fluffer10kFun.items, userData);

		return makeEmbed(title, description);
	}

	private void sendBlueprintDetails(final MessageComponentInteraction in, final int page,
			final ServerUserData userData, final BlacksmithBlueprint blueprint, final BlacksmithTier tier) {
		sendBlueprintDetails(in, page, userData, blueprint, tier, "");
	}

	private void sendBlueprintDetails(final MessageComponentInteraction in, final int page,
			final ServerUserData userData, final BlacksmithBlueprint blueprint, final BlacksmithTier tier,
			final String prepend) {
		final EmbedBuilder embed = blueprintDetailsEmbed(userData, blueprint, prepend);
		final boolean useDisabled = !blueprint.resources.payable(fluffer10kFun.items, userData);

		final ModularPrompt prompt = new ModularPrompt(embed, //
				button("Use", ButtonStyle.SUCCESS, in2 -> useBlueprint(in2, page, userData, blueprint, tier),
						useDisabled), //
				button("Back", ButtonStyle.DANGER, in2 -> onBack(in2, page, userData, tier)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, in);
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final BlacksmithTier tier) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(page, userData, tier), in);
	}

	private void useBlueprint(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final BlacksmithBlueprint blueprint, final BlacksmithTier tier) {
		afterItemPicked(in, page, userData, blueprint, tier, new ArrayList<>(), new ArrayList<>());
	}

	private void afterItemPicked(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final BlacksmithBlueprint blueprint, final BlacksmithTier tier, final List<Payer> payers,
			final List<Item> pickedItems) {
		if (userData.blacksmith.blueprints.get(blueprint.id) < 1) {
			sendBlueprintDetails(in, page, userData, blueprint, tier, "You don't have such blueprint.\n\n");
			return;
		}

		for (int i = payers.size(); i < blueprint.resources.resources.size(); i++) {
			final BlacksmithBlueprintResource resource = blueprint.resources.resources.get(i);
			if (resource.isPickable()) {
				resource.pick(fluffer10kFun, in, userData, (interaction2, page2, itemAmount) -> {
					payers.add(new ItemPayer(itemAmount.item.id, itemAmount.amount));
					pickedItems.add(itemAmount.item);
					afterItemPicked(interaction2, page2, userData, blueprint, tier, payers, pickedItems);
				});
				return;
			} else {
				final Payer payer = resource.getPayer();
				if (!payer.canPay(userData)) {
					sendBlueprintDetails(in, page, userData, blueprint, tier, "You don't have enough materials.\n\n");
					return;
				}

				payers.add(payer);
			}
		}

		for (final Payer payer : payers) {
			if (!payer.canPay(userData)) {
				sendBlueprintDetails(in, page, userData, blueprint, tier, "You don't have enough materials.\n\n");
				return;
			}
		}

		for (final Payer payer : payers) {
			payer.pay(userData);
		}

		final Item item = blueprint.itemGenerator.generate(fluffer10kFun, pickedItems);
		userData.addItem(item);

		sendBlueprintDetails(in, page, userData, blueprint, tier, "Crafting successful!\n\n");
	}
}
