package vos;


import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Representa una reserva sirve entre intermediario entre un cliente y un operador 
 */
public class Reserva {
	//Atributos
	/**
	 * Fecha inicio de la reserva
	 */
	@JsonProperty(value="FechaInicial")
	protected String FechaInicial;
	/**
	 * Fecha final de reserva
	 */
	@JsonProperty(value="FechaFinal")
	protected String FechaFinal;
	
	/**
	 * Ultima fecha pa cancelar reserva antes de penalizacion
	 */
	@JsonProperty(value="FcehaConvenienteDeCancelacion")
	protected String FechaConvenienteDeCanelacion;
	//Asociacion
	/**
	 * Representa las propuesta con una reserva
	 */
	@JsonProperty(value="Propuesta")
	protected Propuesta Propuesta;
	/**
	 *Identificador de una reserva cada identificador es unica 
	 */
	@JsonProperty(value="id")
	protected Long id;

	//Cosntructor 
	public Reserva(@JsonProperty(value="FechaConvenienteDeCancelacion")String fechaConvenineteDeCancelacion,@JsonProperty(value="FechaInicial")String fechaInicial,@JsonProperty(value="FechaFinal")String fechaFinal,	@JsonProperty(value="id")Long id,@JsonProperty(value="Propuesta")Propuesta propuesta)
	{
		this.FechaConvenienteDeCanelacion=fechaConvenineteDeCancelacion;
		this.FechaInicial=fechaInicial;
		this.FechaFinal=fechaFinal;
		
		this.id=id;
		if(propuesta!=null)
		this.Propuesta=propuesta;
	}

	//Metodo getter y setter
	

	/**
	 * 
	 * @return propuesta asociada a una reserva
	 */
	public Propuesta getPropuesta() {
		return this.Propuesta;
	}

	/**
	 * 
	 * @return id de una reserva
	 */
	public long getId() {
		return this.id;
	}



	public String getFechaInicial() {
		return FechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		FechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return FechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		FechaFinal = fechaFinal;
	}

	public String getFechaConvenienteDeCanelacion() {
		return FechaConvenienteDeCanelacion;
	}

	public void setFechaConvenienteDeCanelacion(String fechaConvenienteDeCanelacion) {
		FechaConvenienteDeCanelacion = fechaConvenienteDeCanelacion;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @param myPropuesta nueva propuesta asociada a una reserva
	 */
	public void setPropuesta(Propuesta myPropuesta) {
		this.Propuesta = myPropuesta;
	}




}



