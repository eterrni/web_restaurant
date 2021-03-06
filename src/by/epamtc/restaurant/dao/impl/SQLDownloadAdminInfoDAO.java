package by.epamtc.restaurant.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.restaurant.bean.order.Order;
import by.epamtc.restaurant.bean.order.OrderStatus;
import by.epamtc.restaurant.bean.payment.Payment;
import by.epamtc.restaurant.bean.payment.PaymentStatus;
import by.epamtc.restaurant.bean.user.User;
import by.epamtc.restaurant.dao.DownloadAdminInfoDAO;
import by.epamtc.restaurant.dao.exception.DAOException;
import by.epamtc.restaurant.dao.impl.connection_pool.ConnectionPool;
import by.epamtc.restaurant.dao.impl.connection_pool.exception.ConnectionPoolException;

public class SQLDownloadAdminInfoDAO implements DownloadAdminInfoDAO {

	private static final String SELECT_UNCONFIRMED_CLIENTS_ORDER = "SELECT * FROM rest_db.order where status='NOT_CONFIRMED';";
	private static final String SELECT_CONFIRMED_CLIENTS_ORDER = "SELECT * FROM rest_db.order where status='CONFIRMED';";
	private static final String SELECT_USERS_LIST = "SELECT * FROM rest_db.users WHERE users_role_id_role = 2;";
	private static final String SELECT_EMPLOYEES_LIST = "SELECT * FROM rest_db.users WHERE users_role_id_role = 1;";
	private static final String SELECT_PAYMENTS_LIST = "SELECT * FROM rest_db.payment;";

	private static final String MESSAGE_SQL_EXCEPTION = "SQLDownloadAdminInfoDAO - SQLException";
	private static final String MESSAGE_CONNECTION_POOL_EXCEPTION = "SQLDownloadAdminInfoDAO - SQLException";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final Logger logger = LogManager.getLogger(SQLDownloadAdminInfoDAO.class);

	/**
	 * A request is being made to display all unconfirmed orders. Each unconfirmed
	 * order is added to the unconfirmedClientsOrderList.
	 * 
	 * @return List of unconfirmed orders
	 * @exception DAOException if occurred severe problem with database
	 */
	@Override
	public List<Order> downloadUnconfirmedClientsOrderList() throws DAOException {
		List<Order> unconfirmedClientsOrderList = new ArrayList<>();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(SELECT_UNCONFIRMED_CLIENTS_ORDER);

			rs = ps.executeQuery();

			while (rs.next()) {
				Order order = new Order();
				order.setId(Integer.parseInt(rs.getString(1)));
				order.setStatus(OrderStatus.valueOf(rs.getString(2)));
				order.setCreateDate(rs.getDate(3));
				order.setUserId(Integer.parseInt(rs.getString(4)));
				unconfirmedClientsOrderList.add(order);
			}

		} catch (SQLException e) {
			logger.error(MESSAGE_SQL_EXCEPTION);
			throw new DAOException(MESSAGE_SQL_EXCEPTION, e);
		} catch (ConnectionPoolException e) {
			logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
			throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps, rs);
			} catch (ConnectionPoolException e) {
				logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
				throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
			}
		}

		return unconfirmedClientsOrderList;
	}

	/**
	 * A request is being made to display all confirmed orders. Each confirmed order
	 * is added to the confirmedClientsOrderList.
	 * 
	 * @return List of confirmed orders
	 * @exception DAOException if occurred severe problem with database
	 */
	@Override
	public List<Order> downloadConfirmedClientsOrderList() throws DAOException {
		List<Order> confirmedClientsOrderList = new ArrayList<>();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(SELECT_CONFIRMED_CLIENTS_ORDER);

			rs = ps.executeQuery();

			while (rs.next()) {
				Order order = new Order();
				order.setId(Integer.parseInt(rs.getString(1)));
				order.setStatus(OrderStatus.valueOf(rs.getString(2)));
				order.setCreateDate(rs.getDate(3));
				order.setUserId(Integer.parseInt(rs.getString(4)));
				confirmedClientsOrderList.add(order);
			}

		} catch (SQLException e) {
			logger.error(MESSAGE_SQL_EXCEPTION);
			throw new DAOException(MESSAGE_SQL_EXCEPTION, e);
		} catch (ConnectionPoolException e) {
			logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
			throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps, rs);
			} catch (ConnectionPoolException e) {
				logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
				throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
			}
		}

		return confirmedClientsOrderList;
	}

	/**
	 * A request is being made to display all users. Each user is added to the
	 * userList.
	 * 
	 * @return List of users
	 * @exception DAOException if occurred severe problem with database
	 */
	@Override
	public List<User> downloadUserList() throws DAOException {
		List<User> userList = new ArrayList<>();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(SELECT_USERS_LIST);

			rs = ps.executeQuery();

			while (rs.next()) {
				User user = new User();

				user.setId(Integer.parseInt(rs.getString(1)));
				user.setName(rs.getString(2));
				user.setSurname(rs.getString(3));
				user.setPatronymic(rs.getString(4));
				user.setPhoneNumber(rs.getString(7));
				user.setAge(Integer.parseInt(rs.getString(8)));
				user.setEmail(rs.getString(9));

				userList.add(user);
			}

		} catch (SQLException e) {
			logger.error(MESSAGE_SQL_EXCEPTION);
			throw new DAOException(MESSAGE_SQL_EXCEPTION, e);
		} catch (ConnectionPoolException e) {
			logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
			throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps, rs);
			} catch (ConnectionPoolException e) {
				logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
				throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
			}
		}

		return userList;
	}

	/**
	 * A request is being made to display all employees. Each employee is added to
	 * the employeeList.
	 * 
	 * @return List of employees
	 * @exception DAOException if occurred severe problem with database
	 */
	@Override
	public List<User> downloadEmployeeList() throws DAOException {
		List<User> employeeList = new ArrayList<>();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(SELECT_EMPLOYEES_LIST);

			rs = ps.executeQuery();

			while (rs.next()) {
				User user = new User();

				user.setId(Integer.parseInt(rs.getString(1)));
				user.setName(rs.getString(2));
				user.setSurname(rs.getString(3));
				user.setPatronymic(rs.getString(4));
				user.setPhoneNumber(rs.getString(7));
				user.setAge(Integer.parseInt(rs.getString(8)));
				user.setEmail(rs.getString(9));

				employeeList.add(user);
			}

		} catch (SQLException e) {
			logger.error(MESSAGE_SQL_EXCEPTION);
			throw new DAOException(MESSAGE_SQL_EXCEPTION, e);
		} catch (ConnectionPoolException e) {
			logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
			throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps, rs);
			} catch (ConnectionPoolException e) {
				logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
				throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
			}
		}

		return employeeList;
	}

	/**
	 * A request is being made to display all payments. Each payment is added to the
	 * paymentList.
	 * 
	 * @return List of payments
	 * @exception DAOException if occurred severe problem with database
	 */
	@Override
	public List<Payment> downloadPaymentList() throws DAOException {
		List<Payment> paymentList = new ArrayList<>();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = connectionPool.takeConnection();
			ps = cn.prepareStatement(SELECT_PAYMENTS_LIST);

			rs = ps.executeQuery();

			while (rs.next()) {
				Payment payment = new Payment();

				payment.setId(rs.getInt(1));
				payment.setStatus(PaymentStatus.valueOf(rs.getString(2)));
				payment.setAmount(rs.getDouble(3));
				payment.setOrderId(rs.getInt(4));

				paymentList.add(payment);
			}

		} catch (SQLException e) {
			logger.error(MESSAGE_SQL_EXCEPTION);
			throw new DAOException(MESSAGE_SQL_EXCEPTION, e);
		} catch (ConnectionPoolException e) {
			logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
			throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
		} finally {
			try {
				connectionPool.closeConnection(cn, ps, rs);
			} catch (ConnectionPoolException e) {
				logger.error(MESSAGE_CONNECTION_POOL_EXCEPTION);
				throw new DAOException(MESSAGE_CONNECTION_POOL_EXCEPTION, e);
			}
		}

		return paymentList;
	}

}
