package vos;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;


public class Operador
{	
	public final static int CAPACIDAD=100;
	public final static int VIVIENDA_U=1;
	public final static int APARTAMENTO=2;
	public final static int HOTEL=3;
	public final static int HOSTAL=4;
	public final static int RESIDENTES_ALEDANIOS=5;

	//Atributos 
	@JsonProperty(value="nombre")
	protected String nombre;
	@JsonProperty(value="tipo")
	protected Integer tipo;
	@JsonProperty(value="capacidad")
	protected Double capacidad;
	@JsonProperty(value="id")
	protected  Long id;
	@JsonProperty(value="minimoTiempo")
	protected  Integer minimoTiempo;
	
	//Asociaciones
	@JsonProperty(value="propuestas")
	protected List<Propuesta> propuestas;



    //Constructor
	public Operador(@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="tipo") int  tipo,
			@JsonProperty(value="capacidad")double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo)
	{
		this.minimoTiempo = minimoTiempo;
		this.nombre=nombre;
		this.tipo=tipo;
		this.capacidad=capacidad;
		this.id=id;
		this.propuestas= new ArrayList<>();
		if(tipo==CAPACIDAD||tipo==VIVIENDA_U||tipo==APARTAMENTO
				   ||tipo==HOSTAL||tipo==HOTEL||tipo==RESIDENTES_ALEDANIOS)
				{
					this.tipo=tipo;
				}
		
	}

	//Metodos get,set
	

	/**
	 * @return the minimoTiempo
	 */
	public Integer getMinimoDeTiempo() {
		return minimoTiempo;
	}

	/**
	 * @param minimoTiempo the minimoTiempo to set
	 */
	public void setMinimoDeTiempo(Integer minimoTiempo) {
		this.minimoTiempo = minimoTiempo;
	}

	public List<Propuesta> getPropuestas() {
		if(this.propuestas == null) {
				this.propuestas = new ArrayList<>();
		}
		return this.propuestas;
	}

	


	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}


	public Double getCapacidad() {
		return this.capacidad;
	}

	public void setCapacidad(Double capacidad) {
		this.capacidad = capacidad;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPropuestas(List<Propuesta> propuestas) {
		this.propuestas = propuestas;
	}

	public long getId() {
		return this.id;
	}
	

}

