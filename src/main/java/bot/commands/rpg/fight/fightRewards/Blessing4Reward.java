package bot.commands.rpg.fight.fightRewards;

import static bot.commands.rpg.quests.Quest.description;
import static bot.commands.rpg.quests.Quest.dialogue;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.apis.MessageUtils.getServer;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.data.fight.PlayerFighterData;

public class Blessing4Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing4Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(getServer(data.channel), user,
				getRandomInt(100, 200), "Helga");

		final String description = String.join("\n", //
				description("You tried so hard, but you failed to win the fight with the incredibly strong minotaur."), //
				dialogue("Ha! Just as I said, you are a weakling! But now I am horny! Feel my furious love!"), //
				description("She furiously milks you as your mind slowly drifts away."), //
				description("You wake up later in your home, your wives cuddling you gently."), //
				dialogue("Hubby was so brave~ We love you so much for that~ Next time you will get her!"), //
				description(
						"You recover and get blessed by the goddess for your bravery, to continue fighting against stronger monsters."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You lost the fight with Helga", description));
	}

}
