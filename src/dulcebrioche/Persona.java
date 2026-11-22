package dulcebrioche;

public class Persona {
    int IdPersona;
    String Nombre, ApellidoPaterno, ApellidoMaterno, Calle, NumeroExterior, NumeroInterior, Colonia, Ciudad, Estado, CodigoPostal, Telefono;

    // Constructor por defecto
    public Persona()
    {
        
    }

    // Constructor sobrecargado
    public Persona(
    
    int IdPersona,        
    String Nombre,
    String ApellidoPaterno,
    String ApellidoMaterno,
    String Calle,
    String NumeroExterior,
    String NumeroInterior,
    String Colonia,
    String Ciudad,
    String Estado,
    String CodigoPostal,
    String Telefono)
{
    this.IdPersona = IdPersona;
    this.Nombre = Nombre;
    this.ApellidoPaterno = ApellidoPaterno;
    this.ApellidoMaterno = ApellidoMaterno;
    this.Calle = Calle;
    this.Ciudad = Ciudad;
    this.CodigoPostal = CodigoPostal;
    this.Colonia = Colonia;
    this.Estado = Estado;
    this.NumeroExterior = NumeroExterior;
    this.NumeroInterior = NumeroInterior;
    this.Telefono = Telefono;
}

    public void setIdPersona(int IdPersona)
    {
        this.IdPersona = IdPersona;
    }
    public int getIdPersona()
    {
        return IdPersona;
    }

    public String getNombre()
    {
        return Nombre;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String Ciudad) {
        this.Ciudad = Ciudad;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

}
