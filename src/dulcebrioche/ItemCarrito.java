package dulcebrioche;

public class ItemCarrito {

    int IdDetalleCarrito, IdProducto, Cantidad; 
    String Nombre;
    double Precio;

    public ItemCarrito(
            int idDetalleCarrito,
            int idProducto,
            String nombre,
            double precio,
            int cantidad)
    {
        this.IdDetalleCarrito = idDetalleCarrito;
        this.IdProducto = idProducto;
        this.Nombre = nombre;
        this.Precio = precio;
        this.Cantidad = cantidad;
    }
    
    public int IdDetalleCarrito() {
        return IdDetalleCarrito;
    }
    public void setIdDetalleCarrito(int IdDetalleCarrito) {
        this.IdDetalleCarrito = IdDetalleCarrito;
    }
    
    public int IdProducto() {
        return IdProducto;
    }
    public void setIdProducto(int IdProducto) {
        this.IdProducto = IdProducto;
    }
    
        public String Nombre() {
        return Nombre;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    public double getSubtotal()
    {
        return Precio * Cantidad;
    }
}