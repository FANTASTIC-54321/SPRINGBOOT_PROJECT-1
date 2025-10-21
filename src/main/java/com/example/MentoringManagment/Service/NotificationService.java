package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.NotificationRequestDTO;
import com.example.MentoringManagment.Entity.Notification;
import com.example.MentoringManagment.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.MentoringManagment.Mapper.NotificationMapper.toEntity;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(@RequestBody NotificationRequestDTO requestDTO) {
        Notification notification = new Notification();
        notification.setReceiverId(requestDTO.getReceiverId());
        notification.setMessage(requestDTO.getMessage());
        notification.setType(requestDTO.getType());
        notification.setCreatedAt(LocalDateTime.now());

        Notification created = toEntity(requestDTO);
        notificationRepository.save(created);
    }

    public List<Notification> getUserNotifications(Long receiverId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId);
    }

    public Long getUnreadCount(Long receiverId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(receiverId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not Found with ID = " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
