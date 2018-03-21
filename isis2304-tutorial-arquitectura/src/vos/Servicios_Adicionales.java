package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Esta clase representa un servicio adicinal , el cual hace parate de una propuesta, ejemplos de servicios adicionales son:
 * -Jacuzzi,piscina,ect..
 */

public class Servicios_Adicionales
{
	/**
	 * Nombre de un servicio adicional
	 * 
	 */
	@JsonProperty(value="Nombre")
	protected String Nombre;
	/**
	 * Costo del servicio adicional si es igual a cero se entiende que no tiene costo adicional 
	 */
	@JsonProperty(value="Costo")
	protected Double Costo;

	/**
	 * Identificador de un servicio adicional cada id es unico
	 */
	@JsonProperty(value="id")
	protected Long id;

	public Servicios_Adicionales(@JsonProperty(value="Nombre")String nombre,@JsonProperty(value="Costo") double costo,@JsonProperty(value="id") Long id) 
	{
		
		this.Nombre = nombre;
		this.Costo = costo;
		this.id = id;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public Double getCosto() {
		return Costo;
	}

	public void setCosto(Double costo) {
		Costo = costo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



}

