package com.ehrbridge.hospital.auth.service;

import com.ehrbridge.hospital.auth.dto.GenerateConsentRequest;
import com.ehrbridge.hospital.auth.dto.GenerateConsentResponse;
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

        var consentObject = ConsentObjectHIU.builder()
                .patient_ehbr_id(request.getConsentObject().getEhrbID())
                .hiu_id(request.getConsentObject().getHiuID())
                .hip_id(request.getConsentObject().getHipID())
                .doctor_ehbr_id(request.getConsentObject().getDoctorId())
                .hi_type(Arrays.toString(request.getConsentObject().getHiType()))
                .departments(Arrays.toString(request.getConsentObject().getDepartments()))
                .date_from(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsentObject().getPermisisons().getJSONObject("dateRange").getString("from")))
                .date_to(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsentObject().getPermisisons().getJSONObject("dateRange").getString("to")))
                .validity(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsentObject().getPermisisons().getString("validity")))
                .build();

        consentObjectRepository.save(consentObject);

//        Long consentObjectID  = consentObject.getConsent_object_id();

        var consent_transaction = ConsentTransaction.builder()
                        .consent_status("PENDING")
                        .consent_object_id(consentObject)
                        .build();
        consentTransactionRepository.save(consent_transaction);



        Long consentRequestId = consent_transaction.getConsent_request_id();

        return GenerateConsentResponse.builder().consent_request_id(consentRequestId).message("Consent Generated Successfully").build();
    }
}
