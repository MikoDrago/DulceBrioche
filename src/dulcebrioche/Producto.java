package dulcebrioche;

public class Producto {

    String NombreProducto, Categoria, Descripcion;
    double Precio;
    int IdProducto, Stock, DiasAnticipacion;
    
    public Producto(){
    }
    
    public Producto (int IdProducto, String NombreProducto, String Categoria, double Precio, int Stock, int DiasAnticipacion, String Descripcion)
    {
        this.IdProducto = IdProducto;
        this.NombreProducto = NombreProducto;
        this.Categoria = Categoria;
        this.Descripcion = Descripcion;
        this. Precio = Precio;
        this.Stock = Stock;
        this.DiasAnticipacion = DiasAnticipacion;
    }

    public int getIdProducto() {
        return IdProducto;
    }
    public void setIdProducto(int IdProducto) {
        this.IdProducto = IdProducto;
    }
    
    public String getNombreProducto() {
        return NombreProducto;
    }
    public void setNombreProducto(String NombreProducto) {
        this.NombreProducto = NombreProducto;
    }

    public String getCategoria() {
        return Categoria;
    }
    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public double getPrecio() {
        return Precio;
    }
    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    public int getDiasAnticipacion() {
        return DiasAnticipacion;
    }

    public void setDiasAnticipacion(int DiasAnticipacion) {
        this.DiasAnticipacion = DiasAnticipacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}