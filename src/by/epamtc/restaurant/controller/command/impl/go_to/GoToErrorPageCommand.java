package by.epamtc.restaurant.controller.command.impl.go_to;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.restaurant.controller.command.Command;

public class GoToErrorPageCommand implements Command {

	private static final String ERROR_PAGE = "WEB-INF/jsp/error_page.jsp";
	private static final String ATTRIBUTE_AUTHORIZATION_MESSAGE = "authorization_message";
	private static final String CONTENT_TYPE = "text/html";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);

		request.getRequestDispatcher(ERROR_PAGE).forward(request, response);

		request.getSession().removeAttribute(ATTRIBUTE_AUTHORIZATION_MESSAGE);
	}

}
