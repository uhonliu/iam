package it.infn.mw.iam.notification;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.infn.mw.iam.core.IamDeliveryStatus;
import it.infn.mw.iam.core.time.TimeProvider;
import it.infn.mw.iam.persistence.model.IamEmailNotification;
import it.infn.mw.iam.persistence.repository.IamEmailNotificationRepository;

@Service
public class DefaultNotificationStoreService implements NotificationStoreService {

  public static final Logger LOG = LoggerFactory
      .getLogger(DefaultNotificationStoreService.class);
  
  final IamEmailNotificationRepository repo;
  final TimeProvider timeProvider;
  final NotificationProperties properties;

  @Autowired
  public DefaultNotificationStoreService(IamEmailNotificationRepository repo,
      TimeProvider timeProvider, NotificationProperties properties) {
    this.repo = repo;
    this.timeProvider = timeProvider;
    this.properties = properties;
  }

  @Override
  public void clearExpiredNotifications() {
    Date currentTime = new Date(timeProvider.currentTimeMillis());
    Date threshold = DateUtils.addDays(currentTime, -properties.getCleanupAge());

    List<IamEmailNotification> messageList =
        repo.findByStatusWithUpdateTime(IamDeliveryStatus.DELIVERED, threshold);

    if (!messageList.isEmpty()) {
      repo.delete(messageList);
      LOG.info("Deleted {} messages in status {} older than {}", messageList.size(),
          IamDeliveryStatus.DELIVERED, threshold);
    }
  }


  @Override
  public int countPendingNotifications() {
    return repo.countByDeliveryStatus(IamDeliveryStatus.PENDING);
  }

  @Override
  public void clearAllNotifications() {
    repo.deleteAll();
  }

}
