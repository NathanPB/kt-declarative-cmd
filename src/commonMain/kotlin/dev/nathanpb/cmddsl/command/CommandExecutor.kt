package dev.nathanpb.cmddsl.command

abstract class CommandExecutor<T: CommandPayload> {
    abstract val descriptor: CommandDescriptor

    abstract fun run(payload: T)
}
