import dev.nathanpb.cmddsl.command.CommandDescriptor
import dev.nathanpb.cmddsl.command.CommandPayload
import dev.nathanpb.cmddsl.router.CommandRouter
import java.time.LocalDateTime

val helpEmbed = ""

interface Channel {
    val id: String

    fun sendMessage(string: String)
}

interface User {
    val id: String
    val name: String
    val privateChannel: Channel
    fun ping(): Int

    fun createProfileEmbed(): String
}

data class Message(
    val sender: User,
    val content: String,
    val at: LocalDateTime,
    val channel: Channel,
) : CommandPayload

val router = CommandRouter<Message>()

fun main() {
    router.route {
        describe(CommandDescriptor("ping")) {
            execute { (sender, _, _, channel) ->
                channel.sendMessage("Latencia: ${sender.ping()}")
            }
        }
        describe(CommandDescriptor("help")) {
            execute { (_, _, _, channel) ->
                channel.sendMessage(helpEmbed)
            }
        }
        describe(CommandDescriptor("profile")) {
            describe(CommandDescriptor("view")) {
                describe(CommandDescriptor("mine")) {
                    execute { (sender) ->
                        sender.privateChannel.sendMessage(sender.createProfileEmbed())
                    }
                }

                /* execute { (sender), args ->
                   sender.privateChannel.sendMessage(getProfileOf(args["target"]))
                } */
            }
            describe(CommandDescriptor("follow")) {
                /* execute { context, args ->
                    context.sender.follow(args["target"])
                } */
            }
        }
    }
    println(router)
}
