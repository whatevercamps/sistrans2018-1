package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Apartamentos extends Operadores
{
	//Atributos
	/**
	 * Indica si el apratamento se encuentra amoblado
	 */
	@JsonProperty(value="Muebles")
	protected Boolean Muebles;

	public Apartamentos(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") Integer tipo,
			@JsonProperty(value="MinDeTiempo") Double minTiempo,
			@JsonProperty(value="Capacidad")Double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="Muebles")Boolean muebles)
	{
	super(nombre, tipo, minTiempo, capacidad, id);
	this.Muebles=muebles;
	
	}

	public Boolean getMuebles() {
		return Muebles;
	}

	public void setMuebles(Boolean muebles) {
		Muebles = muebles;
	}
	


}

