package bot.data;

import static bot.util.CollectionUtils.mapMap;
import static bot.util.CollectionUtils.mapMapString;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.server.Server;

import bot.commands.casino.CasinoJackpot;
import bot.commands.rpg.harem.CommandHarem;
import bot.commands.rpg.shop.CommandShop;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.util.Utils;
import bot.util.apis.APIUtils;

public class ServerData {
	public Long modRoleId = null;
	public long jackpotStake = 0;
	public Long botChannelId = null;
	public Long lastJobMessageId = null;
	public boolean lastJobMessageIdWrong = false;
	public long goldenCookieCountdown = 0;
	public int goldenCookiesCaught = 0;
	public Long lastGoldenCookieMessageId = null;

	public Map<MonsterGirlRace, Long> monmusuMarket = new HashMap<>();
	public Map<String, Long> shopItems = new HashMap<>();

	public ServerData() {
		CommandHarem.populateMarketOnNewServer(this);
		CommandShop.addItemsOnNewServer(this);
	}

	@SuppressWarnings("unchecked")
	public ServerData(final Map<String, Object> data) {
		modRoleId = longFromNumber(data.get("modRoleId"));
		jackpotStake = longFromNumber(data.getOrDefault("jackpotStake", 0));
		botChannelId = longFromNumber(data.get("botChannelId"));
		lastJobMessageId = longFromNumber(data.get("lastJobMessageId"));
		lastJobMessageIdWrong = "true".equals(data.get("lastJobMessageIdWrong"));
		goldenCookieCountdown = longFromNumber(data.getOrDefault("goldenCookieCountdown", 0));
		goldenCookiesCaught = intFromNumber(data.getOrDefault("goldenCookiesCaught", 0));
		lastGoldenCookieMessageId = longFromNumber(data.get("lastGoldenCookieMessageId"));

		monmusuMarket = mapMap(((Map<String, Object>) data.getOrDefault("monmusuMarket", new HashMap<>())),
				MonsterGirlRace::valueOf, Utils::longFromNumber);
		shopItems = mapMapString((Map<String, Object>) data.getOrDefault("shopItems", new HashMap<>()),
				Utils::longFromNumber);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("modRoleId", modRoleId);
		map.put("jackpotStake", jackpotStake);
		map.put("botChannelId", botChannelId);
		map.put("lastJobMessageId", lastJobMessageId);
		map.put("lastJobMessageIdWrong", lastJobMessageIdWrong);
		map.put("goldenCookieCountdown", goldenCookieCountdown);
		map.put("goldenCookiesCaught", goldenCookiesCaught);
		map.put("lastGoldenCookieMessageId", lastGoldenCookieMessageId);

		map.put("monmusuMarket", monmusuMarket);
		map.put("shopItems", shopItems);

		return map;
	}

	public boolean checkBotChannel(final long serverId, final APIUtils apiUtils) {
		if (botChannelId == null) {
			return false;
		}
		if (apiUtils.messageUtils.getChannelById(botChannelId) == null) {
			botChannelId = null;
			final Server server = apiUtils.api.getServerById(serverId).orElse(null);
			final String serverName = server == null ? "null" : server.getName();
			apiUtils.messageUtils.sendMessageToMe("No bot channel for server " + serverId + ": " + serverName);
			return false;
		}

		return true;
	}

	public CompletableFuture<Message> sendMessageOnBotChannel(final long serverId, final APIUtils apiUtils,
			final MessageBuilder msg) {
		if (!checkBotChannel(serverId, apiUtils)) {
			return null;
		}

		return apiUtils.messageUtils.sendMessageOnServerChannel(botChannelId, msg);
	}

	public void removeMessageFromBotChannel(final long serverId, final APIUtils apiUtils, final long messageId) {
		if (!checkBotChannel(serverId, apiUtils)) {
			return;
		}

		try {
			final ServerTextChannel channel = apiUtils.messageUtils.getChannelById(botChannelId);
			if (channel != null) {
				final Message lastMsg = apiUtils.messageUtils.getMessageById(channel, messageId);
				if (lastMsg != null) {
					lastMsg.delete();
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public void addJackpotStake(final long amount) {
		jackpotStake += amount;
		if (jackpotStake > CasinoJackpot.maxStake) {
			jackpotStake = CasinoJackpot.maxStake;
		}
	}
}
