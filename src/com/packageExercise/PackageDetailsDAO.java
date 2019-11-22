package com.packageExercise;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PackageDetailsDAO {

	public List<PackageDetails> findAll() {
		List<PackageDetails> list = new ArrayList<PackageDetails>();
		Connection c = null;
		String sql = "SELECT * FROM package ORDER BY package";
		try {
			c = ConnectionHelper.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				list.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return list;
	}

	public List<PackageDetails> findByName(String name) {
		List<PackageDetails> list = new ArrayList<PackageDetails>();
		Connection c = null;
		String sql = "SELECT * FROM package as e " + "WHERE UPPER(name) LIKE ? " + "ORDER BY name";
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "%" + name.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return list;
	}

	public PackageDetails findById(int id) {
		String sql = "SELECT * FROM package WHERE id = ?";
		PackageDetails pkg = null;
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				pkg = processRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return pkg;
	}

	public PackageDetails save(PackageDetails pkg) {
		return pkg.getId() > 0 ? update(pkg) : create(pkg);
	}

	public PackageDetails create(PackageDetails pkg) {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement("INSERT INTO package (name, products, description, price) VALUES (?, ?, ?, ?)",
					new String[] { "ID" });
			ps.setString(1, pkg.getName());
			Object[] objects = new Object[] { pkg.getProducts() };
			Array array = c.createArrayOf("STRING", objects);
			ps.setArray(2, array);
			ps.setString(3, pkg.getDescription());
			ps.setFloat(4, pkg.getPrice());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			// Update the id in the returned object. This is important as this value must be
			// returned to the client.
			int id = rs.getInt(1);
			pkg.setId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return pkg;
	}

	public PackageDetails update(PackageDetails pkg) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE package SET name=?, products=?, description=?, price=? WHERE id=?");
			ps.setString(1, pkg.getName());
			Object[] objects = new Object[] { pkg.getProducts() };
			Array array = c.createArrayOf("STRING", objects);
			ps.setArray(2, array);
			ps.setString(3, pkg.getDescription());
			ps.setFloat(4, pkg.getPrice());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return pkg;
	}

	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM package WHERE id=?");
			ps.setInt(1, id);
			int count = ps.executeUpdate();
			return count == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	}

	protected PackageDetails processRow(ResultSet rs) throws SQLException {
		PackageDetails pkg = new PackageDetails();
		pkg.setId(rs.getInt("id"));
		pkg.setName(rs.getString("name"));
		Array a = rs.getArray("products");
		String[] products = (String[]) a.getArray();
		pkg.setProducts(products);
		pkg.setDescription(rs.getString("description"));
		pkg.setPrice(rs.getFloat("price"));
		return pkg;
	}

}
