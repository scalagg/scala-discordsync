package gg.minequest.sync.conditions

import gg.minequest.sync.profile.DiscordProfileOrchestrator
import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/12/2022
 */
@Service
@IgnoreAutoScan
object LinkedAnnouncementCondition : AnnouncementDisplayCondition
{
    override fun key() = Key
        .key("sync", "account-linked")

    override fun shouldDisplay(player: Player): Boolean
    {
        val profile = DiscordProfileOrchestrator.find(player)
            ?: return true

        return profile.discordId == null
    }
}
