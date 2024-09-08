package com.backend.lms.service;

import org.springframework.stereotype.Service;

@Service
public interface ISMSService {

    void verifyNumber(String number);
    void sendSms(String toMobileNumber, String message);

}