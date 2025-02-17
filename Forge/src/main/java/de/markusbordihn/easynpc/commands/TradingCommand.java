/*
 * Copyright 2023 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easynpc.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.markusbordihn.easynpc.entity.EasyNPCEntity;
import de.markusbordihn.easynpc.entity.EntityManager;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class TradingCommand extends CustomCommand {

  public static ArgumentBuilder<CommandSourceStack, ?> register() {
    return Commands.literal("trading")
        .then(
            Commands.literal("open")
                .requires(
                    commandSourceStack ->
                        commandSourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(
                    Commands.argument("uuid", UuidArgument.uuid())
                        .suggests(TradingCommand::suggestEasyNPCs)
                        .executes(
                            context ->
                                open(context.getSource(), UuidArgument.getUuid(context, "uuid")))))
        .then(
            Commands.literal("reset")
                .requires(
                    commandSourceStack ->
                        commandSourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(
                    Commands.argument("uuid", UuidArgument.uuid())
                        .suggests(TradingCommand::suggestEasyNPCs)
                        .executes(
                            context ->
                                reset(
                                    context.getSource(), UuidArgument.getUuid(context, "uuid")))));
  }

  private static int open(CommandSourceStack context, UUID uuid) throws CommandSyntaxException {
    ServerPlayer serverPlayer = context.getPlayerOrException();
    if (uuid == null) {
      return 0;
    }

    // Try to get the EasyNPC entity by UUID.
    EasyNPCEntity easyNPCEntity = EntityManager.getEasyNPCEntityByUUID(uuid, serverPlayer);
    if (easyNPCEntity == null) {
      context.sendFailure(new TextComponent("EasyNPC with UUID " + uuid + " not found!"));
      return 0;
    }

    easyNPCEntity.openTradingScreen(serverPlayer);

    return Command.SINGLE_SUCCESS;
  }

  private static int reset(CommandSourceStack context, UUID uuid) throws CommandSyntaxException {
    ServerPlayer serverPlayer = context.getPlayerOrException();
    if (uuid == null) {
      return 0;
    }

    // Try to get the EasyNPC entity by UUID.
    EasyNPCEntity easyNPCEntity = EntityManager.getEasyNPCEntityByUUID(uuid, serverPlayer);
    if (easyNPCEntity == null) {
      context.sendFailure(new TextComponent("EasyNPC with UUID " + uuid + " not found!"));
      return 0;
    }

    // Check is player is owner of the EasyNPC.
    if (!serverPlayer.isCreative() && !easyNPCEntity.isOwner(serverPlayer)) {
      context.sendFailure(new TextComponent("You are not the owner of this EasyNPC!"));
      return 0;
    }

    context.sendSuccess(new TextComponent("Resetting trading offers for " + easyNPCEntity), false);
    easyNPCEntity.resetTradingOffers();

    return Command.SINGLE_SUCCESS;
  }
}
