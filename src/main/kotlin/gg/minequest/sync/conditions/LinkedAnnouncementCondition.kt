package gg.minequest.sync.conditions

import gg.minequest.sync.profile.DiscordProfileOrchestrator
import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import gg.scala.flavor.service.Service
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/12/2022
 */
@Service
object LinkedAnnouncementCondition : AnnouncementDisplayCondition
{
    override fun key() = AnnouncementKey(
        namespace = "discord",
        value = "linked"
    )

    override fun shouldDisplay(player: Player): Boolean
    {
        val profile = DiscordProfileOrchestrator.find(player)
            ?: return true

        return profile.discordId != null
    }
}
