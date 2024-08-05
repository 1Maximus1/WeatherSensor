package org.client.sensorResponse;


import lombok.Data;
import org.client.dto.MeasurementDTO;

import java.util.List;

@Data
public class MeasurementsResponse {
    List<MeasurementDTO> measurements;
}
