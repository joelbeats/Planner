package se.joeladonai.userapi.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import se.joeladonai.userapi.exception.RepositoryException;
import se.joeladonai.userapi.exception.ServiceException;
import se.joeladonai.userapi.model.Issue;
import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.model.WorkItem.WorkItemStatus;
import se.joeladonai.userapi.repository.IssueRepository;
import se.joeladonai.userapi.repository.WorkItemRepository;

@Service
public class IssueService {

	private final IssueRepository issueRepository;
	private final WorkItemRepository workItemRepository;

	@Autowired
	public IssueService(IssueRepository issueRepository, WorkItemRepository workItemRepository) {
		this.issueRepository = issueRepository;
		this.workItemRepository = workItemRepository;
	}
	
	@Transactional
	public Issue createIssueAndAssignToWorkItem(Issue issue, Long workItemId)
			throws ServiceException, RepositoryException {

		if (!workItemRepository.exists(workItemId)) {
			throw new ServiceException("WorkItem with id: " + workItemId + " doesn't exist");
		}
		
		if (workItemRepository.findOne(workItemId).getStatus() != WorkItemStatus.DONE) {
			throw new ServiceException("Cannot add issue to workItem because workItem is not status DONE");
		}

		try {
			WorkItem workItem = workItemRepository.findOne(workItemId);
			workItem.setIssue(issue);
			workItem.setStatus(WorkItemStatus.UNSTARTED);
			workItemRepository.save(workItem);
			return issue;

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public Issue updateIssue(Long id, Issue issue) throws ServiceException, RepositoryException {

		if (!issueRepository.exists(id)) {
			throw new ServiceException("Issue with id: " + id + " doesn't exist");
		}

		try {
			Issue newIssue = issueRepository.findOne(id);

			newIssue.setTitle(issue.getTitle());
			newIssue.setDescription(issue.getDescription());
			return issueRepository.save(newIssue);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public Issue getIssueById(Long id) throws ServiceException, RepositoryException {

		if (!issueRepository.exists(id)) {
			throw new ServiceException("Issue with id: " + id + " doesn't exist");
		}

		try {
			return issueRepository.findOne(id);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

}