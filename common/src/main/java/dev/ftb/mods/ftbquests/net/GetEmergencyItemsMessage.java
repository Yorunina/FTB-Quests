package dev.ftb.mods.ftbquests.net;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class GetEmergencyItemsMessage extends BaseC2SMessage {
	GetEmergencyItemsMessage(FriendlyByteBuf buffer) {
	}

	public GetEmergencyItemsMessage() {
	}

	private static final Object2LongMap<UUID> lastItemsGot = new Object2LongOpenHashMap<>();

	@Override
	public MessageType getType() {
		return FTBQuestsNetHandler.GET_EMERGENCY_ITEMS;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		ServerPlayer player = (ServerPlayer) context.getPlayer();
		long now = Util.getEpochMillis();
		long delta = now - lastItemsGot.getOrDefault(player.getUUID(), 0L);

		if (delta >= ServerQuestFile.INSTANCE.getEmergencyItemsCooldown() * 1000L) {
			ServerQuestFile.INSTANCE.getEmergencyItems()
					.forEach(stack -> ItemStackHooks.giveItem(player, stack.copy()));
			lastItemsGot.put(player.getUUID(), now);
		}
	}
}