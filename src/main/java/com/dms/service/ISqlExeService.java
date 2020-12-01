package com.dms.service;


import com.dms.entity.RsSqlExeRecord;
import org.springframework.stereotype.Service;

@Service
public interface ISqlExeService {
    void sqlExeAsync(RsSqlExeRecord rsSqlExeRecord, String sql);

    void sqlExe(RsSqlExeRecord rsSqlExeRecord, String sql);
}
