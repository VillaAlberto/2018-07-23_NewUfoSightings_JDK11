package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Arco;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void loadAllStates(Map<String, State> mappaStati) {
		String sql = "SELECT DISTINCT * FROM state";
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (!mappaStati.containsKey(rs.getString("id").toLowerCase()))
				{
				State state = new State(rs.getString("id").toLowerCase(), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				mappaStati.put(rs.getString("id").toLowerCase(), state);
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	

	public List<String> loadAllShapes(int anno) {
		String sql = "SELECT DISTINCT shape FROM sighting WHERE YEAR(DATETIME)=? ORDER BY shape";
		List<String> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				list.add(res.getString(1));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}
	
	public List<Arco> loadAllArchi(int anno, Map<String, State> mappaStati, String shape) {
		String sql = "SELECT s1.state, s2.state, COUNT(*) AS peso FROM sighting s1, sighting s2, neighbor WHERE s1.state<s2.state AND s1.id!=s2.id AND s1.state=neighbor.state1 AND s2.state=neighbor.state2 AND YEAR(s1.datetime)=? AND YEAR(s2.datetime)=? AND s1.shape=? AND s2.shape=? GROUP BY s1.state, s2.state";
		List<Arco> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setString(3, shape);
			st.setString(4, shape);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				list.add(new Arco(mappaStati.get(res.getString(1).toLowerCase()), mappaStati.get(res.getString(2).toLowerCase()), res.getInt(3)));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<Sighting> listaEventi(int anno, String shape) {
		String sql = "SELECT * FROM sighting WHERE YEAR(datetime)=? AND shape=?";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state").toLowerCase(), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<String> tuttiGliStati() {
		String sql = "SELECT DISTINCT * FROM state";
		List<String> ls= new LinkedList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				ls.add(rs.getString("id").toLowerCase());
			
			}

			conn.close();
			return ls;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> StatiVicini(String partenza) {
		String sql = "SELECT DISTINCT state2 FROM neighbor WHERE state1=?";
		List<String> ls= new LinkedList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, partenza.toUpperCase());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				ls.add(rs.getString(1).toLowerCase());
			
			}

			conn.close();
			return ls;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}

