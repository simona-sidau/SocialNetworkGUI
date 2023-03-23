package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.Request;
import org.example.domain.User;
import org.example.domain.validators.FriendshipValidator;
import org.example.domain.validators.UserValidator;
import org.example.repository.DBFriendshipRepository;
import org.example.repository.DBRequestRepository;
import org.example.repository.DBUserRepository;
import org.example.repository.RepositoryException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Service {

    private final String dbUsername = "postgres";
    private final String dbPassword = "password";
    private final String db = "jdbc:postgresql://localhost:5432/my_social_network";

    DBUserRepository userRepository = new DBUserRepository(db, dbUsername, dbPassword);

    DBFriendshipRepository friendshipRepository = new DBFriendshipRepository(db, dbUsername, dbPassword);

    DBRequestRepository requestRepository = new DBRequestRepository(db, dbUsername, dbPassword);

    UserValidator userValidator = new UserValidator();

    FriendshipValidator friendshipValidator = new FriendshipValidator();


    /**
     * Checks if username and password are correct
     * @param username - String
     * @param password - String
     * @return
     */
    public Optional<User> login(String username, String password) {
        var user = userRepository.findUsername(username);
        if (user != null && user.getPword().equals(password))
            return Optional.of(user);
        else
            return Optional.empty();
    }

    /**
     * Adds a request to database
     * @param req - Request entity
     * @throws ServiceException
     */
    public void addRequest(Request<Integer> req) throws ServiceException {
        try {
            req.setStatus(false);
            requestRepository.add(req);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Search for a Request in database
     * @param entity - Request entity
     * @return - Request entity if it exists, null if not
     */
    public Request<Integer> findRequest(Request<Integer> entity){return requestRepository.find(entity);}
    public User findUserByUserName(String userName){
        return  userRepository.findUsername(userName);
    }

    /**
     * Gets all users from database
     * @return - List with all users
     */
    public List<User> getUsers(){
        return  userRepository.getAll();
    }

    /**
     * Gets all requests from database
     * @return - List with all requests
     */
    public List<Request<Integer>> getRequests(){
        return requestRepository.getAll();
    }

    /**
     * Gets all requests for a user
     * @param owner - User entity
     * @return - List
     */
    public List<Request<Integer>> getUserRequests(User owner){
        return requestRepository.findRequests(owner.getId());
    }

    /**
     * Gets all friends for a user
     * @param owner - User entity
     * @return - List
     */
    public  List<Friendship<UUID>> getFriendsOf(User owner) { return friendshipRepository.getFriends(owner.getId());
    }
    public User findUserById(UUID e) {
        var aux=new User();
        aux.setId(e);
        return userRepository.find(aux);
    }

    /**
     * Add a friendship to database
     * @param friendship - Frienship entity
     * @throws ServiceException
     */
    public void addFriend(Friendship<UUID> friendship) throws ServiceException {
        try {

            friendshipRepository.add(friendship);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Remove a request from database
     * @param found - Request to be deleted
     * @throws ServiceException
     */
    public void deleteRequest(Request<Integer> found) throws ServiceException {
        try {
            requestRepository.delete(found);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Searches for friendship in database
     * @param friendship - Friendship entity
     * @return - Friendship entity if it exists, null if not
     */
    public Friendship<UUID> findFriendship(Friendship<UUID> friendship) {
        return friendshipRepository.find(friendship);
    }

    /**
     * Deletes a Friendship from database
     * @param friendship - Friendship entity
     * @throws ServiceException
     */
    public void deleteFriend(Friendship<UUID> friendship) throws ServiceException {
        try {
            friendshipRepository.delete(friendship);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

}