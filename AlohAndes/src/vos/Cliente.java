package vos;
import java.util.List;

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
	 * codigo que reoresenta a un cliente
	 */
	@JsonProperty(value="codigo")
	protected Long codigo;
	/**
	 * Nmbre de un cliente
	 */
	@JsonProperty(value="nombre")
	protected String nombre;
	/**
	 * apellido de un cliente
	 */	
	@JsonProperty(value="apellido")
	protected String apellido;
	/**
	 * Define la relacion con la entidad tiene que ser alguno de los siguientes:Estudiante; Egresado, Empleado,Profesores,PadresDeEstudiante,ProfesoresInvitados
	 */
	@JsonProperty(value="tipo")
	protected Integer tipo;
	/**
	 * Relacion que permite a un cliente acceder a la lista de sus reservas actuales y antiguas
	 */
	@JsonProperty(value="historia")
	protected List<Factura> historia;

	///Metodo constructor 

	public Cliente(@JsonProperty(value="codigo")Long codigo,
			@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="Appellido")String apellido,
			@JsonProperty(value="tipo")Integer tipo,
			@JsonProperty(value="historia") List<Factura> historia)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.apellido=apellido;	
		if(tipo==ESTUDIANTE||tipo==EGRESADO||tipo==PADRE_DE_ESTUDIANTE
		   ||tipo==EMPLEADO||tipo==PROFESOR||tipo==PROFESOR_INVITADO)
		{
			this.tipo=tipo;
		}
		if(historia!=null)
		{	
			this.historia=historia;
		}
	}
	public Cliente() {
		// TODO Auto-generated constructor stub
	}
	//Metodos getter y setter
	/**
	 * 
	 * @return codigo de el cliente
	 */
	public Long getCodigo() {
		return this.codigo;
	}
	/**
	 * 
	 * @return nombre de el cliente
	 */
	public String getNombre() {
		return this.nombre;
	}
	/**
	 * 
	 * @return apellido del cliente
	 */
	public String getApellido() {
		return this.apellido;
	}
	/**
	 * 
	 * @return tipo de relacion con la insitucion
	 */

	public int getTipo() {
		return this.tipo;
	}
	/**
	 * 
	 * @return contrato de un cliente
	 */
	public List<Factura> getHistoris() {
		return this.historia;
	}
	
	/**
	 * 
	 * @param myCodigo de identificacion 
	 */
	public void setCodigo(Long myCodigo) {
		this.codigo = myCodigo;
	}
	
	/**
	 * 
	 * @param myNombre del cliente
	 */
	public void setNombre(String myNombre) {
		this.nombre = myNombre;
	}
	
	/**
	 * 
	 * @param myApellido apellido del cliente
	 */
	public void setApellido(String myApellido) {
		this.apellido = myApellido;
	}

	/**
	 * 
	 * @param myTipo tipo de relacion con la  institucion
	 */
	public void setTipo(int myTipo) {
		this.tipo = myTipo;
	}

	/**
	 * 
	 * @param myContrato contrato nuevo 
	 */
	public void setContrato(List<Factura> myContrato) {
		this.historia = myContrato;
	}



}



