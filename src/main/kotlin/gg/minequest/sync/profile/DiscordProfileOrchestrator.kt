package gg.minequest.sync.profile

import gg.minequest.sync.MinequestDiscordSync
import gg.minequest.sync.event.ProfileLinkEvent
import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import gg.scala.commons.persist.ProfileOrchestrator
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import io.lettuce.core.api.StatefulRedisConnection
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
@Service
object DiscordProfileOrchestrator : ProfileOrchestrator<DiscordProfile>()
{
    @Inject
    lateinit var plugin: MinequestDiscordSync

    lateinit var connection: StatefulRedisConnection<String, String>

    @Configure
    fun configure()
    {
        val aware = AwareBuilder
            .of<AwareMessage>("discord:sync-feed")
            .codec(AwareMessageCodec)
            .logger(this.plugin.logger)
            .build()

        this.subscribe()

        aware.listen("linked") {
            val playerUniqueId =
                retrieve<UUID>("player-uuid")

            val player = Bukkit.getPlayer(playerUniqueId)
                ?: return@listen

            notifyOfDiscordSync(player)
        }

        Events.subscribe(PlayerJoinEvent::class.java)
            .handler {
                this.notifyOfDiscordSync(it.player)
            }
            .bindWith(plugin)

        aware.connect()

        this.connection = aware.internal().connect()
    }

    private fun notifyOfDiscordSync(player: Player)
    {
        val profile = this.find(player)
            ?: return

        if (
            !profile.notifiedOfSuccessfulLink &&
            profile.discordId != null
        )
        {
            player.sendMessage(
                "${CC.B_GREEN}Great!${CC.GREEN} You've successfully ${
                    if (profile.claimedInitialLinkAward) "re-" else ""
                }synced your Minecraft account to the discord account with the username ${CC.B_AQUA}${
                    profile.lastCachedDiscordUsername
                }${CC.GREEN}. ${CC.B_GOLD}✫"
            )

            if (!profile.claimedInitialLinkAward)
            {
                profile.claimedInitialLinkAward = true
                ProfileLinkEvent(player).flow().subscribe()
            }

            profile.notifiedOfSuccessfulLink = true
            profile.save()
        }
    }

    override fun new(uniqueId: UUID) = DiscordProfile(uniqueId)
    override fun type() = DiscordProfile::class
}
