package org.earthster.client.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.earthster.client.model.Product;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for the management of the in-process database. The database resources
 * are allocated when a new instance of this class is created. A shutdown hook
 * is registered to the runtime which frees these resources. In the normal case
 * there should be only one instance of this class created in an application.
 */
public class Database {

	private static final String PERSISTENCE_UNIT = "earthster";
	private static final String NAME = "derby";

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private EntityManagerFactory emFactory;
	private String path;

	/**
	 * Creates a new database instance using the database under the given
	 * folder. If the database not yet exists it will be created.
	 */
	public Database(String filePath) {
		this.path = filePath;
		start();
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}

	private void start() {
		if (!dbFileExists())
			create();
		String url = url();
		log.info("Connect to database: {}", url);
		Map<Object, Object> props = new HashMap<Object, Object>();
		props.put(PersistenceUnitProperties.JDBC_URL, url);
		try {
			emFactory = new PersistenceProvider().createEntityManagerFactory(
					PERSISTENCE_UNIT, props);
			emFactory.createEntityManager().close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private boolean dbFileExists() {
		File workspace = new File(path);
		File db = new File(workspace, NAME);
		return db.exists() && db.isDirectory();
	}

	private void create() {
		log.info("Create new database");
		try {
			DriverManager.registerDriver(new EmbeddedDriver());
			String url = url() + ";create=true";
			Connection con = DriverManager.getConnection(url);
			SQLScript script = new SQLScript(this.getClass()
					.getResourceAsStream("init.sql"));
			while (script.hasNext()) {
				String stmt = script.next();
				log.trace(stmt);
				con.createStatement().execute(stmt);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private String url() {
		return "jdbc:derby:" + path + "/" + NAME;
	}

	public boolean isConnected() {
		return emFactory != null;
	}

	/** Returns a list of products where the given product is used in an input. */
	public List<Product> whereUsed(Product product) {
		EntityManager em = emFactory.createEntityManager();
		String query = "SELECT a.id FROM Activity a, IN (a.exchanges) exchange"
				+ " WHERE exchange.flow.id = '" + product.getId() + "'";
		try {
			List<String> activityIds = em.createQuery(query, String.class)
					.getResultList();
			List<Product> results = new ArrayList<Product>();
			for (String activityId : activityIds) {
				query = "SELECT p FROM Product p WHERE p.activityId = '"
						+ activityId + "'";
				Product r = em.createQuery(query, Product.class)
						.getSingleResult();
				if (r != null)
					results.add(r);
			}
			return results;
		} catch (Exception e) {
			log.error("Product search failed", e);
			return Collections.emptyList();
		} finally {
			em.close();
		}

	}

	/** Select the entity of the given type with the given ID from the database. */
	public <T> T select(Class<T> clazz, String id) {
		log.trace("Get {} for ID {}", clazz, id);
		EntityManager em = emFactory.createEntityManager();
		try {
			return em.find(clazz, id);
		} catch (Exception e) {
			log.error("Failed to get for id", e);
			return null;
		} finally {
			em.close();
		}
	}

	/**
	 * Returns true if there is an entity of the given type and with the given
	 * ID in the database.
	 */
	public <T> boolean contains(Class<T> clazz, String id) {
		return select(clazz, id) != null;
	}

	/** Get all entities of the given type from the database. */
	public <T> List<T> selectAll(Class<T> clazz) {
		log.trace("Select all of type {}", clazz);
		EntityManager em = emFactory.createEntityManager();
		try {
			String query = "SELECT t FROM " + clazz.getSimpleName() + " t";
			return em.createQuery(query, clazz).getResultList();
		} catch (Exception e) {
			log.error("Select all failed", e);
			return Collections.emptyList();
		} finally {
			em.close();
		}
	}

	/** Insert the given entity in the database. */
	public void insert(Object o) {
		log.trace("insert {}", o);
		EntityManager em = emFactory.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Failed to insert entity", e);
		} finally {
			em.close();
		}
	}

	/** Update the given entity in the database. */
	public void update(Object o) {
		log.trace("update {}", o);
		EntityManager em = emFactory.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Failed to update entity", e);
		} finally {
			em.close();
		}
	}

	/** Delete the given entity from the database. */
	public void delete(Object o) {
		log.trace("delete {}", o);
		EntityManager em = emFactory.createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(em.merge(o));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Failed to delete entity", e);
		} finally {
			em.close();
		}
	}

	private class Shutdown extends Thread {
		@Override
		public void run() {
			log.info("Shutdown database");
			try {
				emFactory.close();
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException e) {
				// a normal shutdown of derby throws an SQL exception
				// with error code 50000 (for single database shutdown
				// 45000), otherwise an error occurred
				if (e.getErrorCode() != 50000)
					log.error(e.getMessage(), e);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
