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

package de.markusbordihn.easynpc.data.action;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;

public class ActionManager {

  private static final HashMap<Mob, EnumMap<ActionGroup, HashSet<ServerPlayer>>>
      actionGroupPlayerMap = new HashMap<>();

  protected ActionManager() {}

  public static void addPlayer(Mob mob, ActionGroup actionGroup, ServerPlayer serverPlayer) {
    if (mob == null || actionGroup == null || serverPlayer == null) {
      return;
    }
    EnumMap<ActionGroup, HashSet<ServerPlayer>> actionGroupPlayer = actionGroupPlayerMap.get(mob);
    if (actionGroupPlayer == null) {
      actionGroupPlayer = new EnumMap<>(ActionGroup.class);
    }
    HashSet<ServerPlayer> playerList = actionGroupPlayer.get(actionGroup);
    if (playerList == null) {
      playerList = new HashSet<>();
    }
    playerList.add(serverPlayer);
    actionGroupPlayer.put(actionGroup, playerList);
    actionGroupPlayerMap.put(mob, actionGroupPlayer);
  }

  public static boolean containsPlayer(
      Mob mob, ActionGroup actionGroup, ServerPlayer serverPlayer) {
    if (mob == null || actionGroup == null || serverPlayer == null) {
      return false;
    }
    EnumMap<ActionGroup, HashSet<ServerPlayer>> actionGroupPlayer = actionGroupPlayerMap.get(mob);
    if (actionGroupPlayer == null) {
      return false;
    }
    HashSet<ServerPlayer> playerList = actionGroupPlayer.get(actionGroup);
    if (playerList == null) {
      return false;
    }
    return playerList.contains(serverPlayer);
  }

  public static void removePlayer(Mob mob, ActionGroup actionGroup, ServerPlayer serverPlayer) {
    if (mob == null || actionGroup == null || serverPlayer == null) {
      return;
    }
    EnumMap<ActionGroup, HashSet<ServerPlayer>> actionGroupPlayer = actionGroupPlayerMap.get(mob);
    if (actionGroupPlayer == null) {
      return;
    }
    HashSet<ServerPlayer> playerList = actionGroupPlayer.get(actionGroup);
    if (playerList == null || !playerList.contains(serverPlayer)) {
      return;
    }
    playerList.remove(serverPlayer);
    actionGroupPlayer.put(actionGroup, playerList);
    actionGroupPlayerMap.put(mob, actionGroupPlayer);
  }

  public static void removeActionGroup(Mob mob, ActionGroup actionGroup) {
    if (mob == null || actionGroup == null) {
      return;
    }
    EnumMap<ActionGroup, HashSet<ServerPlayer>> actionGroupPlayer = actionGroupPlayerMap.get(mob);
    if (actionGroupPlayer == null || !actionGroupPlayer.containsKey(actionGroup)) {
      return;
    }
    actionGroupPlayer.remove(actionGroup);
    actionGroupPlayerMap.put(mob, actionGroupPlayer);
  }
}
