package vos;


import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Representa una reserva sirve entre intermediario entre un cliente y un operador 
 */
public class Reserva {
	//Atributos
	/**
	 * Fecha inicio de la reserva
	 */
	@JsonProperty(value="fechaInicial")
	protected Date fechaInicial;
	/**
	 * Fecha final de reserva
	 */
	@JsonProperty(value="fechaFinal")
	protected Date fechaFinal;
	
	
	//Asociacion
	/**
	 * Representa las propuesta con una reserva
	 */
	@JsonProperty(value="propuesta")
	protected Propuesta propuesta;
	/**
	 *Identificador de una reserva cada identificador es unica 
	 */
	@JsonProperty(value="id")
	protected Long id;

	//Cosntructor 
	public Reserva(
			@JsonProperty(value="fechaInicial")Date fechaInicial,
			@JsonProperty(value="fechaFinal")Date fechaFinal,	
			@JsonProperty(value="id")Long id,
			@JsonProperty(value="Propuesta")Propuesta propuesta)
	{
		this.fechaInicial=fechaInicial;
		this.fechaFinal=fechaFinal;
		
		this.id=id;
		if(propuesta!=null)
		this.propuesta=propuesta;
	}

	//Metodo getter y setter
	

	public Reserva() {
	}

	/**
	 * 
	 * @return propuesta asociada a una reserva
	 */
	public Propuesta getPropuesta() {
		return this.propuesta;
	}

	/**
	 * 
	 * @return id de una reserva
	 */
	public Long getId() {
		return this.id;
	}



	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @param myPropuesta nueva propuesta asociada a una reserva
	 */
	public void setPropuesta(Propuesta myPropuesta) {
		this.propuesta = myPropuesta;
	}




}



