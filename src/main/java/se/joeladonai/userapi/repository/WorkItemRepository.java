package se.joeladonai.userapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.model.WorkItem.WorkItemStatus;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

	List<WorkItem> findByStatus(WorkItemStatus status);

	List<WorkItem> findByIssueIdNotNull();

	List<WorkItem> findByDescriptionContaining(String description);

	List<WorkItem> findByUserId(Long userId);

}
