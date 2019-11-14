package br.carroroubado.app;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import br.carroroubado.api.PesquisaCarroRoubadoServlet;
import br.carroroubado.api.Teste;

public class App {
	public static void main(String[] args) throws Exception {
		InetSocketAddress isa = new InetSocketAddress("0.0.0.0", 8080);
		Server server = new Server(isa);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.setContextPath("/carros");
		handler.setMaxFormContentSize(2 * 1024 * 1024);
		server.setHandler(handler);
		handler.addServlet(PesquisaCarroRoubadoServlet.class, "/pesquisa/*");
		handler.addServlet(Teste.class, "/teste/");
		server.start();
		server.join();
	}
}
