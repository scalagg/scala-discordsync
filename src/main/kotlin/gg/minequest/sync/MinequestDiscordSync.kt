package gg.minequest.sync

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.container.ContainerEnable
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
@Plugin(
    name = "QuestDiscordSync",
    depends = [
        PluginDependency("scala-commons"),
        PluginDependency("store-spigot")
    ]
)
class MinequestDiscordSync : ExtendedScalaPlugin()
