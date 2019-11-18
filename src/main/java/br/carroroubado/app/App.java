package br.carroroubado.app;

import java.net.InetSocketAddress;

import javax.servlet.MultipartConfigElement;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.carroroubado.api.PesquisaCarroRoubadoServlet;
import br.carroroubado.api.Teste;

public class App {
    public static void main(String[] args) throws Exception {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp", 4 * 1024 * 1024, 8 * 1024 * 1024, 2 * 1024 * 1024);

        InetSocketAddress isa = new InetSocketAddress("0.0.0.0", 8080);
        Server server = new Server(isa);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/carros");
        handler.setMaxFormContentSize(2 * 1024 * 1024);
        server.setHandler(handler);

        handler.addServlet(PesquisaCarroRoubadoServlet.class, "/pesquisa/*");
        handler.addServlet(Teste.class, "/teste/");
        handler.addServlet(RegistroPlacaRoubadaServlet.class, "/registro/");

        ServletHolder holderServletVerificar = new ServletHolder(VerificarPlacaServlet.class);
        holderServletVerificar.getRegistration().setMultipartConfig(multipartConfigElement);
        handler.addServlet(holderServletVerificar, "/verificar/");

        server.start();
        server.join();
    }
}
