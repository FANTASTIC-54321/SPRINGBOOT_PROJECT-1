package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification , Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);   //  for listing all notifications

    Long countByReceiverIdAndIsReadFalse(Long receiverId);  // for showing an unread notification count badge, like Instagram.
}


