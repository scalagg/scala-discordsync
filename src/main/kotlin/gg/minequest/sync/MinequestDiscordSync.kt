package gg.minequest.sync

import gg.scala.commons.ExtendedScalaPlugin
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
@Plugin(
    name = "MinequestSync",
    depends = [
        PluginDependency("scala-commons"),
        PluginDependency("store-spigot"),
        PluginDependency("cloudsync", soft = true)
    ]
)
class MinequestDiscordSync : ExtendedScalaPlugin()
