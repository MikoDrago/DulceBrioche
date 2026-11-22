package dulcebrioche;

public class Usuario {

    int IdPersona, IdRol, IdUsuario;

    String Usuario, Correo,  Contrasena;

    public Usuario() {
    }
    
    public Usuario(int IdPersona, int IdRol, int IdUsuario, String Usuario, String Correo,  String Contrasena)
    {
        this.IdPersona = IdPersona;
        this.IdRol = IdRol;
        this.IdUsuario = IdUsuario;
        this.Usuario = Usuario;
        this.Correo = Correo;
        this.Contrasena = Contrasena;
    }
    
    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }
    
    public int getIdPersona() {
        return IdPersona;
    }

    public void setIdPersona(int IdPersona) {
        this.IdPersona = IdPersona;
    }

    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }
}