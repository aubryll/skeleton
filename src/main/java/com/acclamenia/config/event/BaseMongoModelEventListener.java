package com.acclamenia.config.event;

import com.acclamenia.model.base.BaseModel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Annotate decedents
 * with
 * @Component to activate
 * */
public abstract class BaseMongoModelEventListener extends AbstractMongoEventListener<BaseModel> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseModel> event) {
        BaseModel model = event.getSource();
        LocalDateTime localDateTime = LocalDateTime.now();

        if (Objects.nonNull(model.getCreatedAt()))
            model.setUpdatedAt(localDateTime);

        if (Objects.isNull(model.getCreatedAt()))
            model.setCreatedAt(localDateTime);

        if (Objects.isNull(model.getStatus()))
            model.setStatus(BaseModel.Status.ENABLED);

    }


}