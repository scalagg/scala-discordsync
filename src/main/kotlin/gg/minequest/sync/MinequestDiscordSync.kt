package gg.minequest.sync

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite

/**
 * @author GrowlyX
 * @since 8/21/2022
 */
@Plugin(
    name = "MinequestSync",
    version = "%remote%/%branch%/%id%"
)
@PluginAuthor("Scala")
@PluginWebsite("https://scala.gg")

@PluginDependency("scala-commons")
@PluginDependency("store-spigot")
@PluginDependency("cloudsync", soft = true)
@PluginDependency("ScBasics", soft = true)
class MinequestDiscordSync : ExtendedScalaPlugin()
