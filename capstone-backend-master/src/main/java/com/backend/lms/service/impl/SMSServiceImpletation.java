package com.backend.lms.service.impl;

import com.backend.lms.service.ISMSService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.ValidationRequest;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSServiceImpletation implements ISMSService {


    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioFromPhoneNumber;

    @Override
    public void verifyNumber(String number) {
        try {


            if (!number.startsWith("+")) {
                number = "+91" + number;
            }

            Twilio.init(twilioAccountSid, twilioAuthToken);
            ValidationRequest validationRequest =
                    ValidationRequest.creator(new com.twilio.type.PhoneNumber(number))
                            .setFriendlyName("New Phone Number")
                            .create();

            System.out.println(validationRequest.getAccountSid());
        } catch (Exception e) {
            System.out.println("Failed to verify mobile: " + number);
        }
    }

    @Override
    public void sendSms(String toPhoneNumber, String message) {

        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);


            if (!toPhoneNumber.startsWith("+")) {
                toPhoneNumber = "+91" + toPhoneNumber;
            }

            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioFromPhoneNumber),
                    message
            ).create();

        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
