package org.example.repository;

import org.example.domain.Friendship;
import org.example.domain.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBRequestRepository {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public DBRequestRepository(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    public Request<Integer> add(Request<Integer> entity) throws RepositoryException {

        if(find(entity)!=null)
            throw new RepositoryException("Entity exists");
        String sql="insert into requests(user1,user2,date,status) values(?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2, entity.getUser2());
            preparedStatement.setObject(3, entity.getDate());
            preparedStatement.setObject(4, entity.isStatus());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }


    public Request<Integer> delete(Request<Integer> entity) throws RepositoryException {
        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");
        String sql="delete from requests where( user1=? and user2=?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2, entity.getUser2());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    public List<Request<Integer>> findRequests(UUID Receiver) {
        List<Request<Integer>> requestList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwdDb);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM requests where user2=?")){
            preparedStatement.setObject(1, Receiver);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //Integer id = resultSet.getInt("id_request");
                UUID user1 = resultSet.getObject("user1", UUID.class);
                UUID user2 = resultSet.getObject("user2", UUID.class);
                Request<Integer> req = new Request<>(user1, user2);
                //req.setId(id);
                req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                requestList.add(req);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestList;
    }

    public Request<Integer> find(Request<Integer> entity) {
        Request<Integer> req=new Request<>();
        String sql="SELECT * from requests where( user1=? and user2=? )";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2,  entity.getUser2());
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();

            //req.setId(resultSet.getInt("id_request"));
            req.setUser1(resultSet.getObject("user1", UUID.class));
            req.setUser2(resultSet.getObject("user2",UUID.class));
            req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        }catch (SQLException e){

            return null;
        }
        return req;
    }

    public List<Request<Integer>> getAll() {
        List<Request<Integer>> requestList= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM requests");
            ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){
                //Integer id=resultSet.getInt("id_request");
                UUID user1=resultSet.getObject("user1", UUID.class);
                UUID user2=resultSet.getObject("user2", UUID.class);
                Request<Integer> req=new Request<>(user1,user2);
               // req.setId(id);
                req.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                requestList.add(req);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return requestList;
    }


    public int size() {
        int sz=0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM requests");
             ResultSet resultSet= preparedStatement.executeQuery()){
            resultSet.next();
            sz=resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return sz;
    }
}
