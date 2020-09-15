package com.acclamenia.config.netty;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import reactor.netty.http.server.HttpServer;


/**
 * Annotate decedents
 * with
 * @Component activate
 * */
public abstract class AbstractNettyWebServerFactoryPortCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    abstract public int getPort();

    @Override
    public void customize(NettyReactiveWebServerFactory serverFactory) {
        serverFactory.addServerCustomizers(new PortCustomizer(getPort()));
    }

    private static class PortCustomizer implements NettyServerCustomizer {

        private final int port;

        private PortCustomizer(int port) {
            this.port = port;
        }

        @Override
        public HttpServer apply(HttpServer httpServer) {
            return httpServer.port(port);
        }
    }
}

