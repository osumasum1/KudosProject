package kudos.core;

import java.util.Date;

public class Kudo {

	private int fuente;
	private int destino;
	private String tema;
	private Date fecha;
	private String lugar;
	private String texto;
	
	public Kudo() {}
	
	public int getFuente() {
		return fuente;
	}
	public void setFuente(int fuente) {
		this.fuente = fuente;
	}
	public int getDestino() {
		return destino;
	}
	public void setDestino(int destino) {
		this.destino = destino;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
