package com.ehrbridge.hospital.auth.service;

import com.ehrbridge.hospital.auth.dto.consent.GenerateConsentRequest;
import com.ehrbridge.hospital.auth.dto.consent.GenerateConsentResponse;
import com.ehrbridge.hospital.auth.entity.ConsentObjectHIU;
import com.ehrbridge.hospital.auth.entity.ConsentTransaction;
import com.ehrbridge.hospital.auth.repository.ConsentObjectRepository;
import com.ehrbridge.hospital.auth.repository.ConsentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentObjectRepository consentObjectRepository;
    private final ConsentTransactionRepository consentTransactionRepository;
    public GenerateConsentResponse generateConsent(GenerateConsentRequest request) throws JSONException, ParseException {
        System.out.println(request);
        var consentObject = ConsentObjectHIU.builder()
                .patient_ehbr_id(request.getConsent_object().getEhrbID())
                .hiu_id(request.getConsent_object().getHiuID())
                .hip_id(request.getConsent_object().getHipID())
                .doctor_ehbr_id(request.getConsent_object().getDoctorID())
                .hi_type(Arrays.toString(request.getConsent_object().getHiType()))
                .departments(Arrays.toString(request.getConsent_object().getDepartments()))
                .date_from(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getFrom()))
                .date_to(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getTo()))
                .validity(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getConsent_validity()))
                .build();

        consentObjectRepository.save(consentObject);

        var consent_transaction = ConsentTransaction.builder()
                        .consent_status("PENDING")
                        .consent_object_id(consentObject)
                        .build();
        consentTransactionRepository.save(consent_transaction);
        //TODO: Call ABDM Server and store the response(txn_id) in the table - POST Request with consent_object as body.
        Long consentRequestId = consent_transaction.getConsent_request_id();
        return GenerateConsentResponse.builder().consent_request_id(consentRequestId).message("Consent Generated Successfully").build();
    }
}
