package bot.data;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;

import bot.util.apis.APIUtils;

public class Emojis {
	private class FlufferEmoji implements CustomEmoji {
		private final DiscordApi api;
		private final long id;
		private final String name;
		private boolean isAnimated = false;

		public FlufferEmoji(final DiscordApi api, final long id, final String name) {
			this.api = api;
			this.id = id;
			this.name = name;
		}

		public FlufferEmoji animated() {
			isAnimated = true;
			return this;
		}

		@Override
		public DiscordApi getApi() {
			return api;
		}

		@Override
		public long getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isAnimated() {
			return isAnimated;
		}

		@Override
		public Icon getImage() {
			return null;
		}
	}

	public final Emoji fluffytail;
	public final Emoji tamawoo;

	public Emojis(final APIUtils apiUtils) {
		fluffytail = new FlufferEmoji(apiUtils.api, 1444754829007126599L, "fluffytail");
		tamawoo = new FlufferEmoji(apiUtils.api, 1444754805192003738L, "tamawoo");
	}

}
