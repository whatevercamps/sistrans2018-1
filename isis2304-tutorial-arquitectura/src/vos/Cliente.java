package vos;
import javax.*;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * 
 * Clase que representa a un cliente 
 *
 */


public class Cliente {
	
	
	//Constantes de tipo
	private static int ESTUDIANTE=1;
	private static int EGRESADO=2;
	private static int PROFESOR=3;
	private static int EMPLEADO=4;
	private static int PADRE_DE_ESTUDIANTE=5;
	private static int PROFESOR_INVITADO=6;
	//Atributos de la entidad
	/**
	 * Codigo que reoresenta a un cliente
	 */
	@JsonProperty(value="Codigo")
	protected Long Codigo;
	/**
	 * Nmbre de un cliente
	 */
	@JsonProperty(value="Nombre")
	protected String Nombre;
	/**
	 * Apellido de un cliente
	 */	
	@JsonProperty(value="Apellido")
	protected String Apellido;
	/**
	 * Define la relacion con la entidad tiene que ser alguno de los siguientes:Estudiante; Egresado, Empleado,Profesores,PadresDeEstudiante,ProfesoresInvitados
	 */
	@JsonProperty(value="Tipo")
	protected Integer Tipo;
	/**
	 * Relacion que permite a un cliente acceder a una propuesta
	 */
	@JsonProperty(value="Contrato")
	protected Reserva Contrato;

	///Metodo constructor 

	public Cliente(@JsonProperty(value="Codigo")Long codigo,@JsonProperty(value="Nombre")String nombre,@JsonProperty(value="Appellido")String apellido,@JsonProperty(value="Tipo")Integer tipo,@JsonProperty(value="Contrato") Reserva reserva)
	{
		this.Codigo=codigo;
		this.Nombre=nombre;
		this.Apellido=apellido;	
		if(tipo==ESTUDIANTE||tipo==EGRESADO||tipo==PADRE_DE_ESTUDIANTE
		   ||tipo==EMPLEADO||tipo==PROFESOR||tipo==PROFESOR_INVITADO)
		{
			this.Tipo=tipo;
		}
		if(Contrato!=null)
		{	
			this.Contrato=reserva;
		}
	}
	//Metodos getter y setter
	/**
	 * 
	 * @return codigo de el cliente
	 */
	public Long getCodigo() {
		return this.Codigo;
	}
	/**
	 * 
	 * @return nombre de el cliente
	 */
	public String getNombre() {
		return this.Nombre;
	}
	/**
	 * 
	 * @return apellido del cliente
	 */
	public String getApellido() {
		return this.Apellido;
	}
	/**
	 * 
	 * @return tipo de relacion con la insitucion
	 */

	public int getTipo() {
		return this.Tipo;
	}
	/**
	 * 
	 * @return contrato de un cliente
	 */
	public Reserva getContrato() {
		return this.Contrato;
	}
	
	/**
	 * 
	 * @param myCodigo de identificacion 
	 */
	public void setCodigo(Long myCodigo) {
		this.Codigo = myCodigo;
	}
	/**
	 * 
	 * @param myNombre del cliente
	 */
	public void setNombre(String myNombre) {
		this.Nombre = myNombre;
	}
	/**
	 * 
	 * @param myApellido apellido del cliente
	 */
	public void setApellido(String myApellido) {
		this.Apellido = myApellido;
	}

	/**
	 * 
	 * @param myTipo tipo de relacion con la  institucion
	 */
	public void setTipo(int myTipo) {
		this.Tipo = myTipo;
	}

	/**
	 * 
	 * @param myContrato contrato nuevo 
	 */
	public void setContrato(Reserva myContrato) {
		this.Contrato = myContrato;
	}



}



