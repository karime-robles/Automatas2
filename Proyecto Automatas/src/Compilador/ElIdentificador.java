package Compilador;


public class ElIdentificador {

	String nombre;
	String valor;
	String tipo;
	
	
	public ElIdentificador(String nombre, String valor, String tipo) {
		super();
		this.nombre = nombre;
		this.valor = valor;
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
}