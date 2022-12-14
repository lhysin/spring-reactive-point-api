package io.lhysin.point.converter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.lhysin.point.document.Point;
import io.lhysin.point.dto.CreatePointReq;
import io.lhysin.point.type.PointType;

@Component
public class CreatePointReqToPointConverter implements Converter<CreatePointReq, Point> {

    @NonNull
    @Override
    public Point convert(CreatePointReq req) {
        return Point.builder()
            .userId(req.getUserId())
            .amount(req.getAmount())
            .consumedAmount(BigDecimal.ZERO)
            .pointType(PointType.NORMAL)
            .pointCreatedType(req.getPointCreatedType())
            .complete(false)
            .expiredAt(LocalDateTime.now().plusYears(2L))
            .createdAt(LocalDateTime.now())
            .createdBy(req.getCreatedBy())
            .pointTransactions(List.of())
            .build();
    }
}
