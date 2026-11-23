package dulcebrioche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Conecta {
    
    private Connection con = null;

    // Constructor
    public Conecta() {
        conectar();

    }

    // Método para conectar
    public void conectar() {
        try {
            
            // Cargar el driver correcto
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // URL de conexión: usa ?user=...&password=... en lugar de coma
            String url = "jdbc:mysql://localhost:3306/reposteria?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String password = obtenerPassword();
            System.out.println("Usuario: " + user);
            if (password == null || password.isBlank()) {
                throw new SQLException("DB_PASSWORD no está definida en las variables de entorno ni en el archivo .env");
            }
            
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado a la base de datos en MySQL Server");
            System.out.println("Ya esta listo");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Driver JDBC no encontrado: " + cnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error al leer el archivo .env: " + ioe.getMessage());
        } catch (SQLException sqle) {
            System.out.println("Error al conectar: " + sqle.getMessage());
        }
    }

    private String obtenerPassword() throws IOException {
        String password = System.getenv("DB_PASSWORD");
        if (password != null && !password.isBlank()) {
            return password;
        }

        Path archivoEnv = Path.of(".env");
        if (!Files.isRegularFile(archivoEnv)) {
            return null;
        }

        for (String linea : Files.readAllLines(archivoEnv, StandardCharsets.UTF_8)) {
            String contenido = linea.trim();
            if (contenido.isEmpty() || contenido.startsWith("#")) {
                continue;
            }

            int separador = contenido.indexOf('=');
            if (separador < 0 || !"DB_PASSWORD".equals(contenido.substring(0, separador).trim())) {
                continue;
            }

            password = contenido.substring(separador + 1).trim();
            if (password.length() >= 2
                    && ((password.startsWith("\"") && password.endsWith("\""))
                    || (password.startsWith("'") && password.endsWith("'")))) {
                password = password.substring(1, password.length() - 1);
            }
            return password;
        }

        return null;
    }

    public Connection getConnection() {
        return con;
    }
    
    //Cierre del metodo Conectar
    public void desconectar()
    {
        try{
            if(con!=null)
                con.close();
            
            con=null;
            System.out.println("Ya esta desconectado");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    //Inser Persona
public int insertarPersona(Persona obj)
{
    int idPersona = 0;

    try
    {
        PreparedStatement ps = con.prepareStatement(

                "INSERT INTO personas(" +
                "Nombre," +
                "ApellidoPaterno," +
                "ApellidoMaterno," +
                "Telefono," +
                "Calle," +
                "NumeroExterior," +
                "NumeroInterior," +
                "Colonia," +
                "Ciudad," +
                "Estado," +
                "CodigoPostal" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?)",

                Statement.RETURN_GENERATED_KEYS
                );

        ps.setString(1,obj.getNombre());
        ps.setString(2,obj.getApellidoPaterno());
        ps.setString(3,obj.getApellidoMaterno());
        ps.setString(4,obj.getTelefono());
        ps.setString(5,obj.getCalle());
        ps.setString(6,obj.getNumeroExterior());
        ps.setString(7,obj.getNumeroInterior());
        ps.setString(8,obj.getColonia());
        ps.setString(9,obj.getCiudad());
        ps.setString(10,obj.getEstado());
        ps.setString(11,obj.getCodigoPostal());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        if(rs.next())
        {
            idPersona = rs.getInt(1);
        }

        rs.close();
        ps.close();
    }
    catch(SQLException e)
    {
        e.printStackTrace();
    }

    return idPersona;
}
    
    //Inser User
    public void insertarUsuario(Usuario obj)
{
    try
    {
    PreparedStatement ps = con.prepareStatement(
        "INSERT INTO Usuarios(" +
        "IdPersona,IdRol,NombreUsuario," +
        "CorreoElectronico,Contrasena) VALUES(?,?,?,?,?)");
    
        ps.setInt(1,obj.getIdPersona());
        ps.setInt(2,obj.getIdRol());
        ps.setString(3,obj.getUsuario());
        ps.setString(4,obj.getCorreo());
        ps.setString(5,obj.getContrasena());

        ps.executeUpdate();

    }
    catch(SQLException e)
    {
        e.printStackTrace();
    }
}
    
    //Insert Producto
    public int insertarProducto(Producto obj)
    {
        int idProducto = 0;

        String sql =
        "INSERT INTO Productos(" +
        "NombreProducto," +
        "Categoria," +
        "Precio," +
        "Stock," +
        "DiasAnticipacion," +
        "Descripcion" +
        ") VALUES('" +

        obj.getNombreProducto() + "','" +
        obj.getCategoria() + "'," +
        obj.getPrecio() + "," +
        obj.getStock() + "," +
        obj.getDiasAnticipacion() + ",'" +
        obj.getDescripcion() + "')";

        try
        {
            Statement stm = con.createStatement();

            stm.executeUpdate(sql);

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return idProducto;
    }
    
    //Insert Imagen 
    public void insertarImagenProducto(int idProducto,byte[] imagen)
    {
        try
        {
            PreparedStatement ps =con.prepareStatement(

                    "INSERT INTO ImagenesProducto " +
                    "(IdProducto, Imagen) " +
                    "VALUES (?, ?)");

            ps.setInt(1, idProducto);

            ps.setBytes(2, imagen);

            ps.executeUpdate();

            ps.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    //Mostrar Productos
    public ArrayList<Producto> consultarProductos()
    {
        ArrayList<Producto> lista =
                new ArrayList<>();

        try
        {
            Statement stm = con.createStatement();
            ResultSet rs =stm.executeQuery("SELECT * FROM Productos");
                        while(rs.next())
                        {
                            Producto p =new Producto(
                                    rs.getInt("IdProducto"),
                                    rs.getString("NombreProducto"),
                                    rs.getString("Categoria"),
                                    rs.getDouble("Precio"),
                                    rs.getInt("Stock"),
                                    rs.getInt("DiasAnticipacion"),
                                    rs.getString("Descripcion")
                                );

                            lista.add(p);
                        }
            rs.close();
            stm.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public byte[] obtenerPrimeraImagen(int idProducto)
    {
        byte[] imagen = null;
        try
        {
            PreparedStatement ps =
                con.prepareStatement(
                "SELECT Imagen " +
                "FROM ImagenesProducto " +
                "WHERE IdProducto = ? " +
                "LIMIT 1");
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                imagen = rs.getBytes("Imagen");
            }
            rs.close();
            ps.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(imagen != null)
        {
            System.out.println(
                "Imagen encontrada para Producto "
                + idProducto
                + " tamaño="
                + imagen.length);
        }
        else
        {
            System.out.println(
                "SIN IMAGEN para Producto "
                + idProducto);
        }
                return imagen;
    }


    public List<ItemCarrito>obtenerCarrito(int idUsuario)
    {
        List<ItemCarrito> lista =new ArrayList<>();

        try
        {
            String sql =
            "SELECT dc.IdDetalleCarrito, " +
            "p.IdProducto, " +
            "p.NombreProducto, " +
            "p.Precio, " +
            "dc.Cantidad " +
            "FROM carrito c " +
            "INNER JOIN detallecarrito dc " +
            "ON c.IdCarrito = dc.IdCarrito " +
            "INNER JOIN productos p " +
            "ON dc.IdProducto = p.IdProducto " +
            "WHERE c.IdUsuario = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                ItemCarrito item =
            new ItemCarrito( rs.getInt("IdDetalleCarrito"),
                    rs.getInt("IdProducto"), rs.getString("NombreProducto"),
                    rs.getDouble("Precio"), rs.getInt("Cantidad"));
                lista.add(item);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }    

    public boolean eliminarProductoCarrito(
            int idDetalleCarrito)
    {
        try
        {
            String sql = "DELETE FROM detallecarrito WHERE IdDetalleCarrito = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idDetalleCarrito);
            ps.executeUpdate();
            
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public int obtenerOCrearCarrito(int idUsuario)
    {
        int idCarrito = -1;

        try
        {
            String sql =
                    "SELECT IdCarrito " +
                    "FROM carrito " +
                    "WHERE IdUsuario = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                idCarrito = rs.getInt("IdCarrito");
            }
            else
            {
                sql =
                "INSERT INTO carrito(IdUsuario) " +
                "VALUES(?)";

                ps = con.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1,idUsuario);
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();

                if(keys.next())
                {
                    idCarrito = keys.getInt(1);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return idCarrito;
    }

    public boolean agregarProductoCarrito(int idUsuario,int idProducto,int cantidad)
    {
        try
        {
            int idCarrito = obtenerOCrearCarrito(idUsuario);
            String sql =
            "SELECT * " +
            "FROM detallecarrito " +
            "WHERE IdCarrito = ? " +
            "AND IdProducto = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idCarrito);
            ps.setInt(2,idProducto);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                sql =
                "UPDATE detallecarrito " +
                "SET Cantidad = Cantidad + ? " +
                "WHERE IdCarrito = ? " +
                "AND IdProducto = ?";

                ps = con.prepareStatement(sql);

                ps.setInt(1,cantidad);
                ps.setInt(2,idCarrito);
                ps.setInt(3,idProducto);

                ps.executeUpdate();
            }
            else
            {
                sql =
                "INSERT INTO detallecarrito(" +
                "IdCarrito," +
                "IdProducto," +
                "Cantidad) " +
                "VALUES(?,?,?)";

                ps = con.prepareStatement(sql);

                ps.setInt(1,idCarrito);
                ps.setInt(2,idProducto);
                ps.setInt(3,cantidad);

                ps.executeUpdate();
            }

            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean iniciarSesion(String usuarioCorreo,String contrasena)
    {
        try
        {
            String sql =
            "SELECT * " +
            "FROM usuarios " +
            "WHERE (NombreUsuario = ? " +
            "OR CorreoElectronico = ?) " +
            "AND Contrasena = ? " +
            "AND Activo = 1";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuarioCorreo);
            ps.setString(2, usuarioCorreo);
            ps.setString(3, contrasena);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                Sesion.idUsuario = rs.getInt("IdUsuario");
                Sesion.idPersona = rs.getInt("IdPersona");
                Sesion.idRol = rs.getInt("IdRol");
                Sesion.nombreUsuario = rs.getString("NombreUsuario");
                Sesion.correo = rs.getString("CorreoElectronico");
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public double obtenerTotalCarrito(int idUsuario)
    {
        double total = 0;

        try
        {
            String sql =
            "SELECT SUM(p.Precio * dc.Cantidad) Total " +
            "FROM carrito c " +
            "INNER JOIN detallecarrito dc " +
            "ON c.IdCarrito = dc.IdCarrito " +
            "INNER JOIN productos p " +
            "ON dc.IdProducto = p.IdProducto " +
            "WHERE c.IdUsuario = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                total = rs.getDouble("Total");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return total;
    }

    public int generarPedido(int idUsuario)
    {
        try
        {
            int idCarrito =  obtenerOCrearCarrito(   idUsuario);
            double total = obtenerTotalCarrito(   idUsuario);

            String sql =
            "INSERT INTO pedidos(" +
            "IdUsuario," +
            "Total," +
            "IdEstado) " +
            "VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1,idUsuario);
            ps.setDouble(2,total);
            ps.setInt(3,1); // Pendiente
            ps.executeUpdate();
            int idPedido = 0;
            ResultSet keys =  ps.getGeneratedKeys();

            if(keys.next())
            {
                idPedido =
                        keys.getInt(1);
            }

            sql =
            "SELECT dc.IdProducto, " +
            "dc.Cantidad, " +
            "p.Precio " +
            "FROM detallecarrito dc " +
            "INNER JOIN productos p " +
            "ON dc.IdProducto = p.IdProducto " +
            "WHERE dc.IdCarrito = ?";

            ps = con.prepareStatement(sql);
            ps.setInt(1,idCarrito);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int idProducto =
                        rs.getInt("IdProducto");

                int cantidad =
                        rs.getInt("Cantidad");

                double precio =
                        rs.getDouble("Precio");

                PreparedStatement detalle =
                con.prepareStatement(
                "INSERT INTO detallepedido(" +
                "IdPedido," +
                "IdProducto," +
                "Cantidad," +
                "PrecioUnitario) " +
                "VALUES(?,?,?,?)");
                detalle.setInt(1,idPedido);
                detalle.setInt(2,idProducto);
                detalle.setInt(3,cantidad);
                detalle.setDouble(4,precio);
                detalle.executeUpdate();
                PreparedStatement stock =
                con.prepareStatement(
                "UPDATE productos " +
                "SET Stock = Stock - ? " +
                "WHERE IdProducto = ?");

                stock.setInt(1,cantidad);
                stock.setInt(2,idProducto);

                stock.executeUpdate();
            }
            PreparedStatement limpiar =
            con.prepareStatement(
            "DELETE FROM detallecarrito " +
            "WHERE IdCarrito = ?");
            limpiar.setInt(1,idCarrito);
            limpiar.executeUpdate();

            return idPedido;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public List<ItemCarrito> obtenerDetallePedido(
            int idPedido)
    {
        List<ItemCarrito> lista =
                new ArrayList<>();
        try
        {
            String sql =
            "SELECT p.IdProducto, " +
            "p.NombreProducto, " +
            "dp.Cantidad, " +
            "dp.PrecioUnitario " +
            "FROM detallepedido dp " +
            "INNER JOIN productos p " +
            "ON dp.IdProducto = p.IdProducto " +
            "WHERE dp.IdPedido = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                ItemCarrito item =
                        new ItemCarrito(
                                0,
                                rs.getInt("IdProducto"),
                                rs.getString("NombreProducto"),
                                rs.getDouble("PrecioUnitario"),
                                rs.getInt("Cantidad")
                        );

                lista.add(item);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }
    public ArrayList<String[]> obtenerPedidosCliente(int idUsuario)
    {
        ArrayList<String[]> lista = new ArrayList<>();

        try
        {
            String sql =
            "SELECT p.IdPedido, " +
            "p.FechaPedido, " +
            "p.Total, " +
            "e.NombreEstado " +
            "FROM pedidos p " +
            "INNER JOIN estadospedido e " +
            "ON p.IdEstado = e.IdEstado " +
            "WHERE p.IdUsuario = ? " +
            "ORDER BY p.FechaPedido DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                lista.add(
                    new String[]{
                        rs.getString("IdPedido"),
                        rs.getString("FechaPedido"),
                        rs.getString("NombreEstado"),
                        rs.getString("Total")
                    });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<String[]> obtenerDetallePedidoTabla(int idPedido)
    {
        ArrayList<String[]> lista = new ArrayList<>();
        try
        {
            String sql =
            "SELECT p.NombreProducto, " +
            "dp.Cantidad, " +
            "dp.PrecioUnitario " +
            "FROM detallepedido dp " +
            "INNER JOIN productos p " +
            "ON dp.IdProducto = p.IdProducto " +
            "WHERE dp.IdPedido = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idPedido);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                lista.add( new String[]{
                        rs.getString("NombreProducto"),
                        rs.getString("Cantidad"),
                        rs.getString("PrecioUnitario")
                    });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<String[]> obtenerTodosLosPedidos()
    {
        ArrayList<String[]> lista = new ArrayList<>();

        try
        {
            String sql =
            "SELECT " +
            "p.IdPedido, " +
            "u.NombreUsuario, " +
            "p.FechaPedido, " +
            "e.NombreEstado, " +
            "p.Total " +
            "FROM pedidos p " +
            "INNER JOIN usuarios u " +
            "ON p.IdUsuario = u.IdUsuario " +
            "INNER JOIN estadospedido e " +
            "ON p.IdEstado = e.IdEstado " +
            "ORDER BY p.FechaPedido DESC";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next())
            {
                lista.add(
                    new String[]{
                        rs.getString("IdPedido"),
                        rs.getString("NombreUsuario"),
                        rs.getString("FechaPedido"),
                        rs.getString("NombreEstado"),
                        rs.getString("Total")
                    });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizarEstadoPedido(int idPedido,int idEstado)
    {
        try
        {
            String sql =
            "UPDATE pedidos " +
            "SET IdEstado = ? " +
            "WHERE IdPedido = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idEstado);
            ps.setInt(2,idPedido);
            ps.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> obtenerPedidosPerfil(int idUsuario)
    {
        ArrayList<String[]> lista =
                new ArrayList<>();
        try
        {
            String sql =
            "SELECT " +
            "p.IdPedido, " +
            "p.FechaPedido, " +
            "p.Total, " +
            "ep.NombreEstado " +
            "FROM pedidos p " +
            "INNER JOIN estadospedido ep " +
            "ON p.IdEstado = ep.IdEstado " +
            "WHERE p.IdUsuario = ? " +
            "ORDER BY " +
            "CASE " +
            "WHEN ep.NombreEstado = 'Entregado' " +
            "THEN 2 " +
            "ELSE 1 " +
            "END, " +
            "p.FechaPedido DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ResultSet rs =
                    ps.executeQuery();

            while(rs.next())
            {
                lista.add(
                    new String[]{
                        rs.getString("IdPedido"),
                        rs.getString("FechaPedido"),
                        rs.getString("Total"),
                        rs.getString("NombreEstado")
                    });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean eliminarProducto(Producto obj) {
        System.out.println("Eliminando producto: " + obj.getNombreProducto());
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE NombreProducto = ?");
            ps.setString(1, obj.getNombreProducto());

            int filasEliminadas = ps.executeUpdate();
            System.out.println("Numero de registros eliminados: " + filasEliminadas);
            ps.close();

            return filasEliminadas > 0;
        } catch (SQLException sqle) {
            System.out.println("Error al eliminar producto: " + sqle.getMessage());
            sqle.printStackTrace();
        }
        return false;
    }

    public boolean eliminarCuentaCliente(int idPersona) {
        System.out.println("Eliminando cuenta de persona ID: " + idPersona);
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM personas WHERE IdPersona = ?");
            ps.setInt(1, idPersona);

            int filasEliminadas = ps.executeUpdate();
            System.out.println("Numero de registros eliminados: " + filasEliminadas);
            ps.close();

            return filasEliminadas > 0;
        } catch (SQLException sqle) {
            System.out.println("Error al eliminar cuenta: " + sqle.getMessage());
            sqle.printStackTrace();
        }
        return false;
    }

    public boolean actualizarCuenta(Persona p,String correo,String contrasena)
    {
        try
        {
            PreparedStatement ps =con.prepareStatement(
                    "UPDATE personas " +
                    "SET Nombre = ?, " +
                    "ApellidoPaterno = ?, " +
                    "ApellidoMaterno = ?, " +
                    "Telefono = ?, " +
                    "Calle = ?, " +
                    "NumeroExterior = ?, " +
                    "NumeroInterior = ?, " +
                    "Colonia = ?, " +
                    "Ciudad = ?, " +
                    "Estado = ?, " +
                    "CodigoPostal = ? " +
                    "WHERE IdPersona = ?"
                    );

            ps.setString(1,p.getNombre());
            ps.setString(2,p.getApellidoPaterno());
            ps.setString(3,p.getApellidoMaterno());
            ps.setString(4,p.getTelefono());
            ps.setString(5,p.getCalle());
            ps.setString(6,p.getNumeroExterior());
            ps.setString(7,p.getNumeroInterior());
            ps.setString(8,p.getColonia());
            ps.setString(9,p.getCiudad());
            ps.setString(10,p.getEstado());
            ps.setString(11,p.getCodigoPostal());
            ps.setInt(12, Sesion.idPersona);
            ps.executeUpdate();

            if(contrasena != null &&
            !contrasena.trim().isEmpty())
            {
                ps = con.prepareStatement(
                "UPDATE usuarios " +
                "SET CorreoElectronico = ?, " +
                "Contrasena = ? " +
                "WHERE IdUsuario = ?"
                );

                ps.setString(1,correo);
                ps.setString(2,contrasena);
                ps.setInt(3,Sesion.idUsuario);
            }
            else
            {
                ps = con.prepareStatement(
                "UPDATE usuarios " +
                "SET CorreoElectronico = ? " +
                "WHERE IdUsuario = ?"
                );

                ps.setString(1,correo);
                ps.setInt(2,Sesion.idUsuario);
            }

            ps.executeUpdate();
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public Persona LlenarDatosPersona()
    {
        Persona p = null;

        try
        {
            PreparedStatement ps = con.prepareStatement(

                    "SELECT " +
                    "Nombre, " +
                    "ApellidoPaterno, " +
                    "ApellidoMaterno, " +
                    "Telefono, " +
                    "Calle, " +
                    "NumeroExterior, " +
                    "NumeroInterior, " +
                    "Colonia, " +
                    "Ciudad, " +
                    "Estado, " +
                    "CodigoPostal " +
                    "FROM personas " +
                    "WHERE IdPersona = ?"

                    );

            ps.setInt(  1, Sesion.idPersona);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                p = new Persona();
                p.setNombre(rs.getString("Nombre"));
                p.setApellidoPaterno(rs.getString("ApellidoPaterno"));
                p.setApellidoMaterno(rs.getString("ApellidoMaterno"));
                p.setTelefono(rs.getString("Telefono"));
                p.setCalle(rs.getString("Calle"));
                p.setNumeroExterior(rs.getString("NumeroExterior"));
                p.setNumeroInterior(rs.getString("NumeroInterior"));
                p.setColonia(rs.getString("Colonia"));
                p.setCiudad(rs.getString("Ciudad"));
                p.setEstado(rs.getString("Estado"));
                p.setCodigoPostal(rs.getString("CodigoPostal"));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return p;
    }

    public String obtenerCorreo()
    {
        String correo = "";

        try
        {
            PreparedStatement ps = con.prepareStatement(

                    "SELECT CorreoElectronico " +
                    "FROM usuarios " +
                    "WHERE IdUsuario = ?"

                    );

            ps.setInt(1, Sesion.idUsuario);

            ResultSet rs =ps.executeQuery();

            if(rs.next())
            {
                correo =rs.getString("CorreoElectronico");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return correo;
    }

    public void actualizarDatos(Persona p, String correo, String contrasena)
    {
        try
        {
            PreparedStatement ps =con.prepareStatement(
                    "UPDATE personas " +
                    "SET Nombre = ?, " +
                    "ApellidoPaterno = ?, " +
                    "ApellidoMaterno = ?, " +
                    "Telefono = ?, " +
                    "Calle = ?, " +
                    "NumeroExterior = ?, " +
                    "NumeroInterior = ?, " +
                    "Colonia = ?, " +
                    "Ciudad = ?, " +
                    "Estado = ?, " +
                    "CodigoPostal = ? " +
                    "WHERE IdPersona = ?"
                    );
            ps.setString(1,p.getNombre());
            ps.setString(2,p.getApellidoPaterno());
            ps.setString(3,p.getApellidoMaterno());
            ps.setString(4,p.getTelefono());
            ps.setString(5,p.getCalle());
            ps.setString(6,p.getNumeroExterior());
            ps.setString(7,p.getNumeroInterior());
            ps.setString(8,p.getColonia());
            ps.setString(9,p.getCiudad());
            ps.setString(10,p.getEstado());
            ps.setString(11,p.getCodigoPostal());
            ps.setInt(12,Sesion.idPersona);
            ps.executeUpdate();

            ps =con.prepareStatement(
            "UPDATE usuarios " +
            "SET CorreoElectronico = ? " +
            "WHERE IdUsuario = ?"
            );

            ps.setString(1,correo);
            ps.setInt(2,Sesion.idUsuario);
            ps.executeUpdate();
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean actualizarProducto(Producto p,byte[] imagenNueva)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE productos " +
                    "SET NombreProducto = ?, " +
                    "Categoria = ?, " +
                    "Precio = ?, " +
                    "Stock = ?, " +
                    "DiasAnticipacion = ?, " +
                    "Descripcion = ? " +
                    "WHERE IdProducto = ?"
                    );
            ps.setString(1,p.getNombreProducto());
            ps.setString(2,p.getCategoria());
            ps.setDouble(3,p.getPrecio());
            ps.setInt(4,p.getStock());
            ps.setInt(5,p.getDiasAnticipacion());
            ps.setString(6,p.getDescripcion());
            ps.setInt(7,p.getIdProducto());
            ps.executeUpdate();
            // Actualizar imagen si seleccionaron una
            if(imagenNueva != null)
            {
                ps =con.prepareStatement(
                "UPDATE imagenesproducto " +
                "SET Imagen = ? " +
                "WHERE IdProducto = ?"
                );
                ps.setBytes(1,imagenNueva);
                ps.setInt(2,p.getIdProducto());
                int filas =ps.executeUpdate();

                // Si no había imagen previamente

                if(filas == 0)
                {
                    ps =con.prepareStatement(
                    "INSERT INTO imagenesproducto(" +
                    "IdProducto,Imagen" +
                    ") VALUES(?,?)"
                    );
                    ps.setInt(1,p.getIdProducto());
                    ps.setBytes(2,imagenNueva);
                    ps.executeUpdate();
                }
            }
            return true;
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Error al actualizar el producto.",e);
        }
       // return false;
    }

}
