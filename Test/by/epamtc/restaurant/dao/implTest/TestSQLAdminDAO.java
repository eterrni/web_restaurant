package by.epamtc.restaurant.dao.implTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.restaurant.dao.AdminDAO;
import by.epamtc.restaurant.dao.exception.DAOException;
import by.epamtc.restaurant.dao.impl.connection_pool.exception.ConnectionPoolException;
import by.epamtc.restaurant.dao.impl.connection_poolTest.ConnectionPoolTest;

public class TestSQLAdminDAO implements AdminDAO {

	private static final String CHANGE_ORDER_STATUS = "UPDATE `rest_db_test`.`order` SET `status` = 'CONFIRMED' WHERE (`id_order` = ?);";
	private static final String CHANGE_USER_STATUS = "UPDATE `rest_db_test`.`users` SET `users_role_id_role` = '1' WHERE (`id_users` = ?);";
	private static final String CHANGE_EMPLOYEE_STATUS = "UPDATE `rest_db_test`.`users` SET `users_role_id_role` = '2' WHERE (`id_users` = ?);";

	private static final String DISHES_IN_ORDER = "SELECT rest_db_test.order_has_dishes.count, rest_db_test.dishes.price\r\n"
			+ "FROM rest_db_test.order JOIN rest_db_test.order_has_dishes\r\n"
			+ "ON rest_db_test.order.id_order = rest_db_test.order_has_dishes.order_id_order\r\n"
			+ "join rest_db_test.dishes\r\n"
			+ "on rest_db_test.order_has_dishes.dishes_id_dishes = rest_db_test.dishes.id_dishes\r\n"
			+ "where id_order = ?;";
	private static final String DRINKS_IN_ORDER = "SELECT rest_db_test.order_has_drinks.count, rest_db_test.drinks.price\r\n"
			+ "FROM rest_db_test.order JOIN rest_db_test.order_has_drinks\r\n"
			+ "ON rest_db_test.order.id_order = rest_db_test.order_has_drinks.order_id_order\r\n"
			+ "join rest_db_test.drinks\r\n"
			+ "on rest_db_test.order_has_drinks.drinks_id_drinks = rest_db_test.drinks.id_drinks\r\n"
			+ "where id_order = ?;";
	private static final String DESERTS_IN_ORDER = "SELECT rest_db_test.order_has_deserts.count, rest_db_test.deserts.price\r\n"
			+ "FROM rest_db_test.order JOIN rest_db_test.order_has_deserts\r\n"
			+ "ON rest_db_test.order.id_order = rest_db_test.order_has_deserts.order_id_order\r\n"
			+ "join rest_db_test.deserts\r\n"
			+ "on rest_db_test.order_has_deserts.deserts_id_deserts = rest_db_test.deserts.id_deserts\r\n"
			+ "where id_order = ?;";
	private static final String INSERT_PAYMENT = "INSERT INTO `rest_db_test`.`payment` (`status`, `amount`, `order_id_order`) VALUES ('UNPAID', ?, ?);";

	private final ConnectionPoolTest connectionPool = ConnectionPoolTest.getInstance();
	private static final Logger logger = LogManager.getLogger(TestSQLAdminDAO.class);

	@Override
	public void changeOrderStatus(Integer orderId) throws DAOException {

		Connection cn = null;
		PreparedStatement ps = null;

		try {

			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(CHANGE_ORDER_STATUS);

			ps.setInt(1, orderId);

			ps.executeUpdate();

		} catch (SQLException e) {
			logger.error("TestSQLAdminDAO (changeOrderStatus) - SQLException");
			throw new DAOException("TestSQLAdminDAO (changeOrderStatus) - SQLException", e);
		} catch (ConnectionPoolException e) {
			logger.error("TestSQLAdminDAO (changeOrderStatus) - connection pool exception");
			throw new DAOException("TestSQLAdminDAO (changeOrderStatus) - connection pool exception", e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps);
			} catch (ConnectionPoolException e) {
				logger.error("SQLAdminDAO (changeOrderStatus) - connection pool exception");
				throw new DAOException("SQLAdminDAO (changeOrderStatus) - connection pool exception", e);
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void generatePayment(Integer orderId) throws DAOException {

		Double orderAmount = (double) 0;

		Connection cn = null;
		PreparedStatement firstPs = null;
		PreparedStatement secondPs = null;
		PreparedStatement thirdPs = null;
		PreparedStatement fourthPs = null;
		ResultSet rs = null;

		try {

			cn = connectionPool.takeConnection();
			firstPs = cn.prepareStatement(DISHES_IN_ORDER);
			firstPs.setInt(1, orderId);
			rs = firstPs.executeQuery();

			while (rs.next()) {
				Integer countDish = rs.getInt(1);
				Double priceDish = rs.getDouble(2);

				orderAmount += countDish * priceDish;
			}

			secondPs = cn.prepareStatement(DRINKS_IN_ORDER);
			secondPs.setInt(1, orderId);
			rs = secondPs.executeQuery();

			while (rs.next()) {
				Integer countDrink = rs.getInt(1);
				Double priceDrink = rs.getDouble(2);

				orderAmount += countDrink * priceDrink;
			}

			thirdPs = cn.prepareStatement(DESERTS_IN_ORDER);
			thirdPs.setInt(1, orderId);
			rs = thirdPs.executeQuery();

			while (rs.next()) {
				Integer countDesert = rs.getInt(1);
				Double priceDesert = rs.getDouble(2);

				orderAmount += countDesert * priceDesert;
			}

			fourthPs = cn.prepareStatement(INSERT_PAYMENT);
			fourthPs.setDouble(1, orderAmount);
			fourthPs.setInt(2, orderId);
			fourthPs.executeUpdate();

		} catch (SQLException e) {
			logger.error("SQLAdminDAO( generatePayment() )- SQLException");
			throw new DAOException("SQLAdminDAO( generatePayment() ) - SQLException", e);
		} catch (ConnectionPoolException e) {
			logger.error("SQLAdminDAO( generatePayment() ) - ConnectionPoolException");
			throw new DAOException("SQLAdminDAO( generatePayment() ) - ConnectionPoolException", e);
		} finally {
			try {
				connectionPool.closeConnection(cn, firstPs, rs);

				if (secondPs != null) {
					try {
						secondPs.close();
					} catch (SQLException e) {
						logger.error("SQLAdminDAO(generatePayment()) - error close secondPs connection");
						throw new DAOException("SQLAdminDAO( generatePayment() ) - error close secondPs connection", e);
					}
				}

				if (thirdPs != null) {
					try {
						thirdPs.close();
					} catch (SQLException e) {
						logger.error("SQLAdminDAO(generatePayment())-error close thirdPs connection");
						throw new DAOException("SQLAdminDAO(generatePayment())-error close thirdPs connection", e);
					}
				}

				if (fourthPs != null) {
					try {
						fourthPs.close();
					} catch (SQLException e) {
						logger.error("SQLAdminDAO(generatePayment())-error close fourthPs connection");
						throw new DAOException("SQLAdminDAO(generatePayment())-error close fourthPs connection", e);
					}
				}

			} catch (ConnectionPoolException e) {
				logger.error("SQLAdminDAO( generatePayment() ) - error close connection");
				throw new DAOException("SQLAdminDAO( generatePayment() ) - error close connection", e);
			}
		}

	}

	@Override
	public void appointUserAnAdministrator(Integer userId) throws DAOException {
		Connection cn = null;
		PreparedStatement ps = null;

		try {

			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(CHANGE_USER_STATUS);

			ps.setInt(1, userId);

			ps.executeUpdate();

		} catch (SQLException e) {
			logger.error("SQLAdminDAO (appointUserAnAdministrator) - SQLException");
			throw new DAOException("SQLAdminDAO (appointUserAnAdministrator) - SQLException", e);
		} catch (ConnectionPoolException e) {
			logger.error("SQLAdminDAO (appointUserAnAdministrator) - connection pool exception");
			throw new DAOException("SQLAdminDAO (appointUserAnAdministrator) - connection pool exception", e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps);
			} catch (ConnectionPoolException e) {
				logger.error("SQLAdminDAO (appointUserAnAdministrator) - connection pool exception");
				throw new DAOException("SQLAdminDAO (appointUserAnAdministrator) - connection pool exception", e);
			}
		}

	}

	@Override
	public void appointAdministratorAnUser(Integer employeeId) throws DAOException {
		Connection cn = null;
		PreparedStatement ps = null;

		try {

			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(CHANGE_EMPLOYEE_STATUS);

			ps.setInt(1, employeeId);

			ps.executeUpdate();

		} catch (SQLException e) {
			logger.error("SQLAdminDAO (appointAdministratorAnUser) - SQLException");
			throw new DAOException("SQLAdminDAO (appointAdministratorAnUser) - SQLException", e);
		} catch (ConnectionPoolException e) {
			logger.error("SQLAdminDAO (appointAdministratorAnUser) - connection pool exception");
			throw new DAOException("SQLAdminDAO (appointAdministratorAnUser) - connection pool exception", e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps);
			} catch (ConnectionPoolException e) {
				logger.error("SQLAdminDAO (appointAdministratorAnUser) - connection pool exception");
				throw new DAOException("SQLAdminDAO (appointAdministratorAnUser) - connection pool exception", e);
			}
		}

	}
}
