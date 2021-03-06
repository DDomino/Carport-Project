package DBAccess;

import FunctionLayer.CarportException;
import FunctionLayer.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 The purpose of UserMapper is to...

 @author kasper
 */
public class UserMapper {

    public static void createUser( User user ) throws CarportException {
        try {
            Connection con = Connector.connection();
            String SQL = "INSERT INTO Users (User_name, User_password, User_phone, User_email, User_role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
            ps.setString(1,user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getRole());
            ps.executeUpdate();
            ResultSet ids = ps.getGeneratedKeys();
            ids.next();
            int id = ids.getInt( 1 );
            user.setId( id );
        } catch ( SQLException | ClassNotFoundException ex ) {
            throw new CarportException( ex.getMessage() );
        }
    }

    public static User login( String username, String password ) throws CarportException {
        try {
            Connection con = Connector.connection();
            String SQL = "SELECT User_role FROM Users "
                    + "WHERE User_name=? AND User_password=?";
            PreparedStatement ps = con.prepareStatement( SQL );
            ps.setString( 1, username );
            ps.setString( 2, password );
            ResultSet rs = ps.executeQuery();
            if ( rs.next() ) {
                String role = rs.getString( "User_role" );
               
                User user = new User( username, password, role );
            
                return user;
            } else {
                throw new CarportException( "Could not validate user" );
            }
        } catch ( ClassNotFoundException | SQLException ex ) {
            throw new CarportException(ex.getMessage());
        }
    }

}
