const dsl = require('../../../build/js/packages/kt-command-dsl/kotlin/kt-command-dsl').dev.nathanpb.cmddsl
const { CommandRouter } = dsl.router
const { CommandDescriptor } = dsl.command

const router = new CommandRouter()
router.route((dis) => {

  dis.describe(new CommandDescriptor("ping"), function() {
    this.execute((ctx) => {
      ctx.channel.sendMessage(`Latência: ${ctx.sender.ping()}`)
    })
  })

  dis.describe(new CommandDescriptor("help"), (dis) => {
    dis.execute((ctx) => {
      ctx.channel.sendMessage(helpEmbed)
    })
  })

  dis.describe(new CommandDescriptor("profile"), (dis) => {
    dis.describe(new CommandDescriptor("view"), (dis) => {
      dis.describe(new CommandDescriptor("mine"), (dis) => {
        dis.execute((_, ctx) => {
          ctx.sender.privateChannel.sendMessage(createProfileEmbed(ctx.sender))
        })
      })

      dis.execute((_, ctx, args) => {
        ctx.sender.privateChannel.sendMessage(getProfileOf(args.target))
      })
    })

  })
})

console.log(router.toString())

