package se.joeladonai.userapi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import se.joeladonai.userapi.exception.RepositoryException;
import se.joeladonai.userapi.exception.ServiceException;
import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.model.WorkItem.WorkItemStatus;
import se.joeladonai.userapi.repository.TeamRepository;
import se.joeladonai.userapi.repository.UserRepository;
import se.joeladonai.userapi.repository.WorkItemRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final WorkItemRepository workItemRepository;
	private final TeamRepository teamRepository;

	@Autowired
	public UserService(UserRepository userRepository, WorkItemRepository workItemRepository,
			TeamRepository teamRepository) {
		this.userRepository = userRepository;
		this.workItemRepository = workItemRepository;
		this.teamRepository = teamRepository;
	}

	@Transactional
	public User createUser(User user) throws ServiceException, RepositoryException {

		if (user.getUsername().length() < 10) {
			throw new ServiceException("Username too short, must be 10 characters");
		}

		try {
			return userRepository.save(user);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	@Transactional
	public User updateUser(Long id, User user) throws ServiceException, RepositoryException {

		if (!userRepository.exists(id)) {
			throw new ServiceException("User with id: " + id + " doesn't exist");
		}

		if (user.getUsername().length() < 10) {
			throw new ServiceException("Username too short, must be 10 characters");
		}
		if (user.getIsActive() != true) {
			for (WorkItem workItem : workItemRepository.findByUserId(user.getId())) {
				if (workItem.equals(null)) {

				}
				workItem.setStatus(WorkItem.WorkItemStatus.UNSTARTED);
			}

		}

		try {
			User updatedUser = userRepository.findOne(id);
			updatedUser.setIsActive(user.getIsActive());
			updatedUser.setIdNumber(user.getIdNumber());
			updatedUser.setUsername(user.getUsername());
			updatedUser.setFirstname(user.getFirstname());
			updatedUser.setLastname(user.getLastname());
			updatedUser.setTeam(user.getTeam());

			return userRepository.save(updatedUser);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public User getUserById(Long id) throws ServiceException, RepositoryException {

		if (!userRepository.exists(id)) {
			throw new ServiceException("User with id: " + id + " doesn't exist");
		}

		try {
			return userRepository.findOne(id);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}
	
	@Transactional
	public User getUserByName(String username) {
		return userRepository.findByUsername(username);
	}

	public User getUserByIdNumber(Long idNumber) throws ServiceException, RepositoryException {

		try {
			return userRepository.findByIdNumber(idNumber);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public List<User> getUserByUsernameAndFirstnameAndLastname(String username, String firstname, String lastname)
			throws ServiceException, RepositoryException {

		try {
			List<User> users = userRepository.findByUsernameStartingWithAndFirstnameStartingWithAndLastnameStartingWith(
					username, firstname, lastname);

			return users;

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public List<User> getAllUserFromTeam(Long teamId) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(teamId)) {
			throw new ServiceException("Team with team id: " + teamId + " doesn't exist");
		}

		try {
			return userRepository.findByTeamId(teamId);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public User inActivateUser(Long id) throws ServiceException, RepositoryException {

		if (!userRepository.exists(id)) {
			throw new ServiceException("User with id: " + id + " doesn't exist");
		}

		try {
			User user = userRepository.findOne(id);
			user.setIsActive(false);

			List<WorkItem> workItems = workItemRepository.findByUserId(id);

			for (WorkItem workItem : workItems) {
				workItem.setStatus(WorkItemStatus.UNSTARTED);
				workItemRepository.save(workItem);
			}

			return userRepository.save(user);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public User activateUser(Long id) throws ServiceException, RepositoryException {

		if (!userRepository.exists(id)) {
			throw new ServiceException("User with id: " + id + " doesn't exist");
		}

		try {
			User user = userRepository.findOne(id);
			user.setIsActive(true);
			return userRepository.save(user);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public void addWorkItemToUser(Long userId, Long workItemId) throws ServiceException, RepositoryException {

		if (!userRepository.exists(userId)) {
			throw new ServiceException("User does not exist");
		}

		if (userRepository.findOne(userId).getIsActive() == false) {
			throw new ServiceException("Could not add workItem to user because its inactive");
		}
		if (workItemRepository.findByUserId(userId).size() >= 5) {
			throw new ServiceException("Could not add workItem to user because user already has 5 workItems");
		}

		try {
			WorkItem workItem = workItemRepository.findOne(workItemId);
			workItem.setUser(userRepository.findOne(userId));
			workItemRepository.save(workItem);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

}
