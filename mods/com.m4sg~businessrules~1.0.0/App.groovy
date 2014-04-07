println "dafk"
sessionMap = [:]
sessionmanager_address = "com.m4sg.sessionmanager"

eb = vertx.eventBus
log = container.logger
log.info "Iniciando Modulo..."
/*message: {body:{action:[post|get|put], data:{}}, reply:function}
   {body:{action:post, data:{key:XX, value:XX}}}
   {body:{action:get, key:XX}, reply:function}
   {body:{action:delete, key:XX}}
   {body:{action:put, data:{key:XX, value:XX}}}
*/
eb.registerHandler(sessionmanager_address){ message ->
   def json = message.body
   log.info json
   def data = json.data

   switch(json.action) {
      case "post":
      case "put":
         sessionMap[data.key]=data.value
         break

      case "get":
         message.reply(sessionMap[json.key])
         break

      case "delete":
         mesage.remove(json.key)
         break

      case "list":
         sessionMap.each{k,v ->
            log.info "${k} : ${v}"
         }
         break

      default:
         log.error "Action not found ${json.action}"
   }
}

/*
Simulemos un segundo Handler
eb.registerHandler(sessionmanager_address){ message ->
   def json = message.body
   log.info "Desde el 2º Handler: ${json}"
}*/

eb.publish(sessionmanager_address, [action:"post", data:[key:"oluna", value:"123456"]])
eb.publish(sessionmanager_address, [action:"post", data:[key:"jcamara", value:"654321"]])
eb.publish(sessionmanager_address, [action:"list"])
