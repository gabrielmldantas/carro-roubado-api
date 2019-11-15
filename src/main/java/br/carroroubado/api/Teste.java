package br.carroroubado.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Teste extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("AAA");
		resp.addHeader("Content-Type", "application/json");
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("mensagem", new JsonPrimitive("Hello world"));
		resp.getWriter().print(new Gson().toJson(jsonObject));
	}
}

