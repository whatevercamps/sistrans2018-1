package vos;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;


public class Operadores
{	
	private final static int CAPACIDAD=100;
	private static int VIVIENDA_U=1;
	private static int APARTAMENTO=2;
	private static int HOTEL=3;
	private static int HOSTAL=4;
	private static int RESIDENTES_ALEDAÑOS=5;

	//Atributos 
	@JsonProperty(value="Nombre")
	protected String Nombre;
	@JsonProperty(value="Tipo")
	protected Integer Tipo;
	@JsonProperty(value="MinDeTiempo")
	protected Double MinDeTiempo;
	@JsonProperty(value="Capacidad")
	protected Double Capacidad;
	@JsonProperty(value="id")
	protected  Long id;
	
	
	//Asociaciones
	@JsonProperty(value="Propuestas")
	protected List<Propuesta> Propuestas;



    //Constructor
	public Operadores(@JsonProperty(value="Nombre")String nombre,@JsonProperty(value="Tipo") int  tipo,@JsonProperty(value="MinDeTiempo") double minTiempo,@JsonProperty(value="Capacidad")double capacidad,@JsonProperty(value="id") Long id)
	{
		this.Nombre=nombre;
		this.Tipo=tipo;
		this.MinDeTiempo=minTiempo;
		this.Capacidad=capacidad;
		this.id=id;
		this.Propuestas= new ArrayList<>();
		if(tipo==CAPACIDAD||tipo==VIVIENDA_U||tipo==APARTAMENTO
				   ||tipo==HOSTAL||tipo==HOTEL||tipo==RESIDENTES_ALEDAÑOS)
				{
					this.Tipo=tipo;
				}
		
	}

	//Metodos get,set
	

	public List<Propuesta> getPropuestas() {
		if(this.Propuestas == null) {
				this.Propuestas = new ArrayList<>();
		}
		return this.Propuestas;
	}

	


	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public Integer getTipo() {
		return Tipo;
	}

	public void setTipo(Integer tipo) {
		Tipo = tipo;
	}

	public Double getMinDeTiempo() {
		return MinDeTiempo;
	}

	public void setMinDeTiempo(Double minDeTiempo) {
		MinDeTiempo = minDeTiempo;
	}

	public Double getCapacidad() {
		return Capacidad;
	}

	public void setCapacidad(Double capacidad) {
		Capacidad = capacidad;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPropuestas(List<Propuesta> propuestas) {
		Propuestas = propuestas;
	}

	public long getId() {
		return this.id;
	}


	public void addAllPropuestas(Set<Propuesta> newPropuestas) {
		if (this.Propuestas == null) {
			this.Propuestas = new ArrayList<>();
		}
		for (Propuesta tmp : newPropuestas)
			tmp.setOperador(this);
		
	}

	

	
	public void removeAllPropuestas(Set<Propuesta> newPropuestas) {
		if(this.Propuestas == null) {
			return;
		}
		
		this.Propuestas.removeAll(newPropuestas);
	}





	
	
	public void addPropuestas(Propuesta newPropuestas) {
		if(this.Propuestas == null) {
			this.Propuestas = new ArrayList<>();
		}
		
		if (this.Propuestas.add(newPropuestas))
			newPropuestas.basicSetOperador(this);
	}

	

	
	public void removePropuestas(Propuesta oldPropuestas) {
		if(this.Propuestas == null)
			return;
		
		if (this.Propuestas.remove(oldPropuestas))
			oldPropuestas.unsetOperador();
		
	}



}

