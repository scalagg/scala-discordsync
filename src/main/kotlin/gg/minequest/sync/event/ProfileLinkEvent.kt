package gg.minequest.sync.event

import gg.scala.commons.event.StatelessEvent
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
data class ProfileLinkEvent(
    val player: Player
) : StatelessEvent()
