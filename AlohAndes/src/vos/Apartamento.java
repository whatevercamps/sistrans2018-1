package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Apartamento extends Operador
{
	//Atributos
	/**
	 * Indica si el apratamento se encuentra amoblado
	 */
	@JsonProperty(value="muebles")
	protected Boolean muebles;

	public Apartamento(@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="tipo") Integer tipo,
			@JsonProperty(value="capacidad")Double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="muebles")Boolean muebles,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo)
	{
	super(nombre, Operador.APARTAMENTO, capacidad, id, minimoTiempo);
	this.muebles=muebles;
	
	}

	public Boolean getMuebles() {
		return muebles;
	}

	public void setMuebles(Boolean muebles) {
		muebles = muebles;
	}
	


}

