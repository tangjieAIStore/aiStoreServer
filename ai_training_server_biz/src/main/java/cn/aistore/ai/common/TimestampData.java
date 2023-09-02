package cn.aistore.ai.common;

import com.github.houbb.data.factory.api.core.IContext;
import com.github.houbb.data.factory.api.core.IData;

import java.sql.Timestamp;

public class TimestampData implements IData<Timestamp> {

    @Override
    public Timestamp build(IContext iContext, Class<Timestamp> aClass) {
        return new Timestamp(System.currentTimeMillis());
    }
}
