package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.dao.attestation.Attestation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AttestationMapper extends Mapper<Attestation> {

    void insertBatch(@Param("contractList") List<Attestation> attestationList);

}