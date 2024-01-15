package com.pablogormi.magicfoods.networking;

import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.networking.packet.ItemStackSyncS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier ITEM_SYNC = new Identifier(MagicFoods.NAMESPACE, "item_sync");

    public static void registerS2CPackets() {

        ClientPlayNetworking.registerGlobalReceiver(ITEM_SYNC, ItemStackSyncS2CPacket::receive);

    }
}
