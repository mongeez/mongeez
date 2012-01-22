package org.mongeez.examples.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import java.util.Map;

public class MappingJacksonJsonViewEx extends MappingJacksonJsonView {

    public MappingJacksonJsonViewEx() {
        this(new ObjectMapper());
    }

    public MappingJacksonJsonViewEx(ObjectMapper objectMapper) {
        excludeNullsFromPayload(objectMapper);
        doNotFailOnEmptyBeans(objectMapper);
        setObjectMapper(objectMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object filterModel(Map<String, Object> model) {
        Map map = (Map) super.filterModel(model);
        if (map.size() == 1) {
            return map.values().toArray()[0];
        }
        return map;
    }

    public static void excludeNullsFromPayload(ObjectMapper objectMapper) {
        objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public static void doNotFailOnEmptyBeans(ObjectMapper objectMapper) {
        objectMapper.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
    }
}
