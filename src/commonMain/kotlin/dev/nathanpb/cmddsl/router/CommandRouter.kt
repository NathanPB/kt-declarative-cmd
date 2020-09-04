package dev.nathanpb.cmddsl.router

import dev.nathanpb.cmddsl.command.CommandDescriptor
import dev.nathanpb.cmddsl.command.CommandPayload
import kotlin.js.JsName

class CommandRouter<T: CommandPayload> : Node<T>() {

    @JsName("route")
    fun route(body: Node<T>.()->Unit) {
        body(this)
    }
}

sealed class Node<T: CommandPayload> (
    val parent: Node<T>? = null,
    val children: MutableList<Node<T>> = mutableListOf()
) {

    val indentationLevel by lazy {
        var i = 0
        var parent: Node<T>? = parent
        while (parent != null) {
            parent = parent?.parent
            i++
        }
        i
    }

    @JsName("describe")
    fun describe(descriptor: CommandDescriptor, body: DescriptorNode<T>.()->Unit) {
        body (
            DescriptorNode(descriptor, this).also {
                children += it
            }
        )
    }

    override fun toString(): String {
        return children.joinToString("\n").prependIndent("".padStart(indentationLevel * 2))
    }
}

class DescriptorNode<T: CommandPayload>(val descriptor: CommandDescriptor, parent: Node<T>) : Node<T>(parent) {
    private val executors = mutableListOf<CommandDescriptor.(T)->Unit>()

    @JsName("execute")
    fun execute(body: CommandDescriptor.(T)->Unit) {
        executors += body
    }

    override fun toString(): String {
        return "$descriptor: {\n${super.toString()}\n}"
    }
}
