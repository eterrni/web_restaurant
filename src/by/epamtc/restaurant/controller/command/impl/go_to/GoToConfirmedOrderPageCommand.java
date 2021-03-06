package by.epamtc.restaurant.controller.command.impl.go_to;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.restaurant.bean.user.User;
import by.epamtc.restaurant.controller.command.Command;
import by.epamtc.restaurant.controller.command.impl.utility.DownloadAdminInfoUtility;

public class GoToConfirmedOrderPageCommand implements Command {
	private static final DownloadAdminInfoUtility downloadAdminInfoUtility = DownloadAdminInfoUtility.getInstance();
	private static final String WELCOME_PAGE = "WEB-INF/jsp/welcome_page.jsp";
	private static final String CONFIRMED_ORDER_PAGE = "WEB-INF/jsp/admin/confirmed_order_page.jsp";

	private static final String ATTRIBUTE_CONFIRMED_CLIENTS_ORDER_LIST = "confirmed_clients_order_list";
	private static final String ATTRIBUTE_USER = "user";

	private static final Integer USER_ROLE_ID = 2;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(ATTRIBUTE_USER);

		if (user == null || user.getRole().getRoleId() == USER_ROLE_ID) {
			request.getRequestDispatcher(WELCOME_PAGE).forward(request, response);
		} else {

			downloadAdminInfoUtility.downloadConfirmedOrder(request, response);

			request.getRequestDispatcher(CONFIRMED_ORDER_PAGE).forward(request, response);

			session.removeAttribute(ATTRIBUTE_CONFIRMED_CLIENTS_ORDER_LIST);

		}
	}
}
