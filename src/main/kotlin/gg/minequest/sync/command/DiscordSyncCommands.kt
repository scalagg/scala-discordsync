package gg.minequest.sync.command

import gg.minequest.sync.profile.DiscordProfileOrchestrator
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.FancyMessage
import net.md_5.bungee.api.chat.ClickEvent
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.entity.Player
import java.time.Duration
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
@AutoRegister
object DiscordSyncCommands : ScalaCommand()
{
    @CommandAlias("unlink|unsync|unlink-discord-account")
    fun onUnLink(player: Player): CompletableFuture<Void>
    {
        val profile = DiscordProfileOrchestrator.find(player)
            ?: throw ConditionFailedException(
                "Something went terribly wrong while trying to perform discord sync."
            )

        if (profile.discordId == null)
        {
            throw ConditionFailedException("You have not yet synced your discord account! Run ${CC.B_RED}/sync${CC.RED} for additional information or contact an administrator for help.")
        }

        profile.lastCachedDiscordUsername = null
        profile.discordId = null
        profile.notifiedOfSuccessfulLink = false

        return profile.save().thenRun {
            player.sendMessage("${CC.RED}You've unlinked your discord account.")
        }
    }

    @CommandAlias("sync|link")
    fun onSync(player: Player): CompletableFuture<Void>
    {
        val profile = DiscordProfileOrchestrator.find(player)
            ?: throw ConditionFailedException(
                "Something went terribly wrong while trying to perform discord sync."
            )

        return CompletableFuture
            .runAsync {
                if (profile.discordId != null)
                {
                    throw ConditionFailedException("You're already synced to the discord account ${CC.B_RED}${profile.lastCachedDiscordUsername}${CC.RED}!")
                }

                val key = "discord-sync-codes:${profile.identifier}"

                val existing =  DiscordProfileOrchestrator
                    .connection.sync().get(key)

                if (existing != null)
                {
                    throw ConditionFailedException("You have already generated a code! You may reset this code in 10 minutes. Your code is: ${CC.B_RED}$existing")
                }

                val alphanumeric = RandomStringUtils
                    .randomAlphanumeric(7)

                DiscordProfileOrchestrator.connection.sync()
                    .set(key, alphanumeric)

                DiscordProfileOrchestrator.connection.sync()
                    .expire(
                        key, Duration.ofMinutes(10L)
                    )

                FancyMessage()
                    .withMessage(
                        "${CC.GREEN}Your discord sync code is ${CC.B_AQUA}$alphanumeric${CC.GREEN}. This code will expire in 10 minutes! "
                    )
                    .withMessage("${CC.GRAY}(click to copy)")
                    .andHoverOf("${CC.AQUA}Copy your code!")
                    .andCommandOf(
                        ClickEvent.Action.SUGGEST_COMMAND,
                        alphanumeric
                    )
                    .sendToPlayer(player)
            }
    }
}
