package it.polito.tdp.newufosightings.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
private NewUfoSightingsDAO dao;
private Graph<State, DefaultWeightedEdge> grafo;
private Map<String, State> mappaStati;
private Simulator sim;

public Model(){
	dao= new NewUfoSightingsDAO();
}

public List<String> getAllShapes(int anno) {
	// TODO Auto-generated method stub
	return dao.loadAllShapes(anno);
}

public void creaGrafo(int anno, String shape) {
	grafo= new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	//Aggiungo i vertici
	mappaStati= new TreeMap<String, State>();
	dao.loadAllStates(mappaStati);
	Graphs.addAllVertices(grafo, mappaStati.values());
	//Aggiungo gli archi
	
	for (Arco a: dao.loadAllArchi(anno, mappaStati, shape)) {
		System.out.println(a.getS1());
		System.out.println(a.getS2());
		DefaultWeightedEdge e= grafo.addEdge(a.getS1(), a.getS2());
		grafo.setEdgeWeight(e, a.getPeso());
	}
}

public int numeVertex() {
	return grafo.vertexSet().size();
}

public int numEdges() {
	return grafo.edgeSet().size();
}

public List<State> listaVertici(){
	List<State> ls= new LinkedList<State>(grafo.vertexSet());
	ls.sort(new Comparator<State>() {

		@Override
		public int compare(State o1, State o2) {
			// TODO Auto-generated method stub
			return o1.getName().compareTo(o2.getName());
		}
	});
	return ls;
}

public int pesoAdiacenti(State s) {
	int tot=0;
	for (State vicino: Graphs.neighborListOf(grafo, s)) {
		DefaultWeightedEdge e= grafo.getEdge(s, vicino);
		tot+=grafo.getEdgeWeight(e);
	}
	return tot;
}

public Map<String, Double> simula(int t, int alfa, int anno, String shape)
{
	sim= new Simulator(anno, shape);
	sim.simula(t, alfa);
	return sim.getDEFCON();
	
}
}
