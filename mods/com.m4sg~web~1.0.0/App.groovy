log = container.logger

loaders = ["com.m4sg~chat~1.0.0", "com.m4sg~businessrules~1.0.0"]

loaders.each{ moduleName ->
   container.deployModule(moduleName){ asyncResult ->
      if(!asyncResult.succeeded) failLaoadingHandler(moduleName, asyncResult.cause())
      else{
         loaders.remove(moduleName)
         if(!loaders) loadServer()
      } 
   }
}


loadServer = {
   server = vertx.createHttpServer()

   server.requestHandler { req ->
      def file = req.uri == "/" ? "index.html" : req.uri
      req.response.sendFile "web/$file"
   }

   sockJSServer = vertx.createSockJSServer(server)
   sockJSServer.bridge(["prefix": "/eventbus"], [[:]], [[:]])
   server.listen(8080)
   log.info "server running..."
}

failLaoadingHandler = { modName, cause ->
   log.error "fail to load App. Responsible Module ${modName}"
   log.error cause.printStackTrace()
   System.exit(0)
}