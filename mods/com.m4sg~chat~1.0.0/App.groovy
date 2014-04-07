sessionMap = [:]
chat_address = "com.m4sg.chat"

eb = vertx.eventBus
log = container.logger
/*  */
eb.registerHandler(chat_address){ message ->
   def json = message.body
   log.info json
   def data = json.data

   switch(json.action) {
      case "suscribe":
         //sessionMap[data.user] = {msg -> message.reply(msg)}
         break

      case "unsuscribe":
         sessionMap.remove[json.key]
         break

      case "p2p":
         eb.publish(chat_address + ".${data.key}", data.message)
         //sessionMap[data.key](data.message)
         break

      case "broadcast":
         eb.publish(chat_address + ".ALL", json.message)
         break

      default:
         log.error "Action not found ${json.action}"
   }
}
