package vos;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;



/**
 * 
 * Clase que representa una propuesta,una propuesta es quuella que que es ofrecida por un operador ademas se encarga de manejar los servicios. 
 */


public class Propuesta
{
	private static int HABITACION_SENCILLA=1;
	private static int HABITACION_COMPARTIDA=2;
	private static int HABITACION_SUITE_SENCILLA=3;
	private static int HABITACION_SUITE_COMPARTIDA=4;
	private static int HABITACION_SEMI_SUITE_SENCILLA=5;
	private static int  HABITACION_SEMI_SUITE_COMPARTIDA=6;

	/**
	 * Identificador de una propuesta
	 */
	@JsonProperty(value="id")
	protected Long id ;
	@JsonProperty(value="Nombre")
	protected String Nombre;
	@JsonProperty(value="costo")
	protected Double costo;
	@JsonProperty(value="Tipo")
	protected Integer Tipo;
	/**
	 * Representa el operador encargado de una reserva  
	 * 
	 */
	@JsonProperty(value="Operador")
	protected Operador Operador;	
	/**
	 * Representa la lista de servicios adicionales asociadas con una propuesta 
	 * 
	 */
	@JsonProperty(value="Servicios")
	protected List<Servicio> Servicios;
	@JsonProperty(value="reserva")
	protected Reserva reserva;


	///Constructor

	public Propuesta(@JsonProperty(value="Operador") Operador operador,@JsonProperty(value="id")Long id,
			@JsonProperty(value="Nombre") String nombre,@JsonProperty(value="costo") double costo,
			@JsonProperty(value="Tipo") int tipo,@JsonProperty(value="reserva") Reserva reservaA)
	{

		if(operador!=null)
		{
			this.Operador=operador;

		}
		if(reservaA!=null)
		{
			this.reserva=reservaA;

		}
		this.Servicios= new ArrayList<>();
		this.id=id;
		this.Nombre=nombre;
		this.costo=costo;
		if(tipo==HABITACION_SENCILLA||tipo==HABITACION_COMPARTIDA||tipo==HABITACION_SEMI_SUITE_COMPARTIDA
				   ||tipo==HABITACION_SEMI_SUITE_SENCILLA||tipo==HABITACION_SUITE_COMPARTIDA||tipo==HABITACION_SUITE_SENCILLA)
		this.Tipo=tipo;
	}
	// Getters y setters

	/**
	 * 
	 * @param myOperador nuevo operador  responsable de la propuesta
	 */
	public void basicSetOperador(Operador myOperador) {
		if (this.Operador != myOperador) {
			if (myOperador != null){
				if (this.Operador != myOperador) {
					Operador oldOperador = this.Operador;
					this.Operador = myOperador;
					if (oldOperador != null)
						oldOperador.removePropuestas(this);
				}
			}
		}
	}
	


	/**
	 * 
	 * @return el operador relacionado con la propuesta
	 */
	public Operador getOperador() {
		return this.Operador;
	}

	/**
	 * 
	 * @return todos los servicios adicionales asociados a una propuesta
	 */
	public List<Servicio> getServicios() {
		if(this.Servicios == null) {
			this.Servicios = new ArrayList<>();
		}
		return this.Servicios;
	}

	/**
	 * 
	 * @return el identificador de una propuesta
	 */
	public Long getId() {
		return this.id;
	}
	/**
	 * 
	 * @return el tipo de una propuesta
	 */
	public Integer getTipo() {
		return this.Tipo;
	}
	
	/**
	 * 
	 * @return el nombre de una propuesta
	 */
	public String getNombre() {
		return this.Nombre;
	}
	/**
	 * 
	 * @param newServicios nueva lista de servicios adicionales a agregar
	 */

	public void addAllServicios(Set<Servicio> newServicios) {
		if (this.Servicios == null) {
			this.Servicios = new ArrayList<>();
		}
		this.Servicios.addAll(newServicios);
	}
	/**
	 * 
	 * @param newServicios lista de servicios adicionales a elliminar
	 */
	public void removeAllServicios(Set<Servicio> newServicios) {
		if(this.Servicios == null) {
			return;
		}

		this.Servicios.removeAll(newServicios);
	}

	/**
	 * 
	 * @param mySevicioAgua cambio el estado respecto al servicio
	 */



	public void setOperador(Operador myOperador) {
		this.basicSetOperador(myOperador);
		myOperador.addPropuestas(this);
	}

	public void addServicios(Servicio newServicios) {
		if(this.Servicios == null) {
			this.Servicios = new ArrayList<>();
		}

		this.Servicios.add(newServicios);
	}



	public void unsetOperador() {
		if (this.Operador == null)
			return;
		Operador oldOperador = this.Operador;
		this.Operador = null;
		oldOperador.removePropuestas(this);
	}

	public void removeServicios(Servicio oldServicios) {
		if(this.Servicios == null)
			return;

		this.Servicios.remove(oldServicios);
	}

}

