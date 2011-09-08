package org.openstack.atlas.api.helpers.JsonSerializer;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.openstack.atlas.api.helpers.reflection.ClassReflectionTools;
import org.openstack.atlas.api.helpers.reflection.ClassReflectionToolsException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

public class PropertyCollectionMapper extends JsonSerializer<Object> {

    private Map<String, ElementClass> classMap;
    private SerializationConfig config;

    public PropertyCollectionMapper(SerializationConfig config, Map<String, ElementClass> classMap) {
        this.classMap = classMap;
        this.config = config;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Map<String, List<Object>> propMap = new HashMap<String, List<Object>>();
        for (String getterName : classMap.keySet()) {
            try {
                propMap.put(getterName, (List) ClassReflectionTools.invokeGetter(value, getterName));
            } catch (ClassReflectionToolsException ex) {
                String valClassName = value.getClass().getName();
                String format = "Error Failed to dynamicly invoke %s.%s() during serialization of %s";
                String errMsg = String.format(format, valClassName, getterName, value.toString());
                throw new org.codehaus.jackson.JsonGenerationException(errMsg, ex);
            }
        }

        jgen.writeStartObject();
        for (String getterName : classMap.keySet()) {
            jgen.writeFieldName(classMap.get(getterName).getElementName());
            jgen.writeStartArray();
            for (Object childObj : propMap.get(getterName)) {
                childSerialize(childObj, jgen);
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();

    }

    public static class ElementClass {

        private String getterName;
        private String elementName;
        private Class objClass;

        public ElementClass(String getterName, String elementName) {
            this.getterName = getterName;
            this.elementName = elementName;
        }

        public String getElementName() {
            return elementName;
        }

        public void setElementName(String elementName) {
            this.elementName = elementName;
        }

        public String getGetterName() {
            return getterName;
        }

        public void setGetterName(String getterName) {
            this.getterName = getterName;
        }
    }

    private void childSerialize(Object obj, JsonGenerator jgen) throws JsonProcessingException, IOException {
        SerializerProviderBuilder providerBuilder = new SerializerProviderBuilder();
        //BeanSerializerFactory csf = BeanSerializerFactory.instance;
        CustomSerializerFactory csf = new CustomSerializerFactory();
        csf.addSpecificMapping(GregorianCalendar.class, new DateTimeSerializer(config, null));
        SerializerProvider childProvider;
        JavaType childType = TypeFactory.type(obj.getClass());
        BasicBeanDescription childBeanDesc = this.config.introspect(childType);
        JsonSerializer<Object> childSerializer = csf.findBeanSerializer(childType, config, childBeanDesc);
        childProvider = providerBuilder.createProvider(config, csf);
        childSerializer.serialize(obj, jgen, childProvider);
    }
}
