package se.joeladonai.userapi.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class WorkItem extends AbstractEntity {

	public enum WorkItemStatus {
		UNSTARTED, STARTED, DONE
	}

	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	private WorkItemStatus status = WorkItemStatus.UNSTARTED;

	@ManyToOne()
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	private Issue issue;

	public WorkItem(String title, String description) {
		this.title = title;
		this.status = WorkItem.WorkItemStatus.UNSTARTED;
		this.description = description;
	}

	protected WorkItem() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WorkItemStatus getStatus() {
		return status;
	}

	public void setStatus(WorkItemStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;

	}
}