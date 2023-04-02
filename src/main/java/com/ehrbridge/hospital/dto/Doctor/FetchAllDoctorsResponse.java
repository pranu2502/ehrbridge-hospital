package com.ehrbridge.hospital.dto.Doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.ehrbridge.hospital.entity.Doctor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchAllDoctorsResponse {
    private List<Doctor> doctors;
}