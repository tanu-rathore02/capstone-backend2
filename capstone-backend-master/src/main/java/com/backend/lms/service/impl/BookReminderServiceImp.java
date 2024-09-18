package com.backend.lms.service.impl;

import com.backend.lms.model.Books;
import com.backend.lms.model.Issuances;
import com.backend.lms.model.Users;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.IssuancesRepository;
import com.backend.lms.repository.UsersRepository;
import com.backend.lms.service.IBookReminderService;
import com.backend.lms.service.ISMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookReminderServiceImp implements IBookReminderService {

    private final IssuancesRepository issuanceRepository;
    private final ISMSService ismsService;
    //    second minute hour day-of-month month day-of-week
//    @Scheduled(cron = "0 0 15 * * *", zone = "Asia/Kolkata")
    @Override
//    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata") // Every day at 00:00 (midnight)
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void sendReturnRemainder() {
        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);
        List<Issuances> dueTomorrow = issuanceRepository.findAllByReturnDateBetweenAndStatus(startOfTomorrow, endOfTomorrow, "Issued");
        System.out.println("SCHEDULER CALLED" + dueTomorrow);
        for (Issuances issuance : dueTomorrow) {
            String message = String.format("\nReminder:\n" +
                            "Please return the book '%s'\n" +
                            "Author '%s'\n"+
                            "by tomorrow (%s).",
                    issuance.getBooks().getTitle(), issuance.getBooks().getAuthor(),
                    issuance.getReturnDate().toLocalDate());

            System.out.println(dueTomorrow);
//       ismsService.sendSms(issuance.getUsers().getMobileNumber(), message);
        }
    }
}
