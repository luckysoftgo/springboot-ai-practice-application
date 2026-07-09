package com.application.ai.practice.model.enums;

public enum FilterTypeEnum {
    ISEQUALTO("1","相等"),
    CONTAINSSTRING("2","包含字符串"),
    ISBETWEEN("3","在两者之间"),
    ISGREATERTHAN ("4","大于"),
    ISGREATERTHANOREQUALTO ("5","大于或等于"),
    ISIN("6","在XXX之中"),
    ISLESSTHAN("7","小于"),
    ISLESSTHANOREQUALTO("8","小于或等于"),
    ISNOTEQUALTO("9","不等于"),
    ISNOTIN("10","不在XXX之中")
    ;

    private String type;
    private String desc;

    FilterTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FilterTypeEnum getType(String type){
        FilterTypeEnum filterTypeEnum = null;
        for (FilterTypeEnum typeEnum : FilterTypeEnum.values()){
            if (typeEnum.type.equalsIgnoreCase(type)){
                filterTypeEnum = typeEnum;
            }
        }
        return filterTypeEnum;
    }
}
