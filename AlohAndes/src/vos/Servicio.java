package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Esta clase representa un servicio adicinal , el cual hace parate de una propuesta, ejemplos de servicios adicionales son:
 * -Jacuzzi,piscina,ect..
 */

public class Servicio
{
	/**
	 * nombre de un servicio adicional
	 * 
	 */
	@JsonProperty(value="nombre")
	protected String nombre;
	/**
	 * costo del servicio adicional si es igual a cero se entiende que no tiene costo adicional 
	 */
	@JsonProperty(value="costo")
	protected Double costo;

	/**
	 * Identificador de un servicio adicional cada id es unico
	 */
	@JsonProperty(value="id")
	protected Long id;

	public Servicio(@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="costo") double costo,
			@JsonProperty(value="id") Long id) 
	{
		
		this.nombre = nombre;
		this.costo = costo;
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



}

