package se.joeladonai.userapi.service;

import java.util.ArrayList;
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
public class WorkItemService {

	private final WorkItemRepository workItemRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	
	WorkItem workItem;

	@Autowired
	public WorkItemService(WorkItemRepository workItemRepository, UserRepository userRepository,
			TeamRepository teamRepository) {
		this.workItemRepository = workItemRepository;
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
	}

	@Transactional
	public WorkItem createWorkItem(WorkItem workItem) throws ServiceException, RepositoryException {

		try {
			return workItemRepository.save(workItem);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	@Transactional
	public WorkItem updateWorkItem(Long id, String title, String description)
			throws ServiceException, RepositoryException {

		if (!workItemRepository.exists(id)) {
			throw new ServiceException("WorkItem with id: " + id + " doesn't exist");
		}

		try {
			WorkItem workItem = workItemRepository.findOne(id);
			workItem.setTitle(title);
			workItem.setDescription(description);
			return workItemRepository.save(workItem);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public WorkItem getWorkItemById(Long id) throws ServiceException, RepositoryException {

		if (!workItemRepository.exists(id)) {
			throw new ServiceException("WorkItem with id: " + id + " doesn't exist");
		}

		try {
			return workItemRepository.findOne(id);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public WorkItem changeWorkItemStatus(Long id, WorkItemStatus status) throws ServiceException, RepositoryException {

		if (!workItemRepository.exists(id)) {
			throw new ServiceException("WorkItem with id: " + id + " doesn't exist");
		}

		try {
			WorkItem workItem = workItemRepository.findOne(id);
			workItem.setStatus(status);
			workItemRepository.save(workItem);
			return workItem;

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public void removeWorkItem(Long id) throws ServiceException, RepositoryException {

		if (!workItemRepository.exists(id)) {
			throw new ServiceException("WorkItem with id: " + id + " doesn't exist");
		}

		try {
			workItemRepository.delete(id);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public List<WorkItem> getWorkItemsByStatus(WorkItemStatus status) throws RepositoryException, ServiceException {

		try {
			return workItemRepository.findByStatus(status);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public List<WorkItem> getWorkItemsWithIssue() throws ServiceException, RepositoryException {

		try {
			return workItemRepository.findByIssueIdNotNull();

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public List<WorkItem> getWorkItemsByDescription(String description) throws ServiceException, RepositoryException {

		try {
			return workItemRepository.findByDescriptionContaining(description);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public List<WorkItem> getAllWorkItemsByUser(Long userId) throws ServiceException, RepositoryException {

		if (!userRepository.exists(userId)) {
			throw new ServiceException("User with user id: " + userId + " doesn't exist");
		}

		try {
			return workItemRepository.findByUserId(userId);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public List<WorkItem> getAllWorkItemsByTeam(Long teamId) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(teamId)) {
			throw new ServiceException("WorkItem with id: " + teamId + " doesn't exist");
		}

		try {
			List<User> users = userRepository.findByTeamId(teamId);

			List<WorkItem> workItemsFromUsers = new ArrayList<>();
			for (User user : users) {

				List<WorkItem> workItems = workItemRepository.findByUserId(user.getId());
				for (WorkItem workItem : workItems) {
					workItemsFromUsers.add(workItem);
				}
			}
			return workItemsFromUsers;

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}
}	