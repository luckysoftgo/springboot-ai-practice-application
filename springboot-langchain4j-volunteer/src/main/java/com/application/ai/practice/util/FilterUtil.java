package com.application.ai.practice.util;

import com.application.ai.practice.model.dto.FilterInfo;
import com.application.ai.practice.model.enums.FilterTypeEnum;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import dev.langchain4j.store.embedding.filter.comparison.IsNotEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsNotIn;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterUtil {

    public static List<Filter> generate(List<FilterInfo> filters){
        List<Filter> filterList = new ArrayList<>();
        if (CollectionUtils.isEmpty(filters)){
            return filterList;
        }
        for (FilterInfo info : filters){
            String type = info.getType();
            FilterTypeEnum typeEnum = FilterTypeEnum.getType(type);
            switch (typeEnum){
                case ISIN -> {
                    filterList.add(new IsIn(info.getKey(), Arrays.asList(info.getValue())));
                }
                case ISNOTIN ->{
                    filterList.add( new IsNotIn(info.getKey(), Arrays.asList(info.getValue())));
                }
                case ISBETWEEN -> {
                    // 需要单独处理
                }
                case ISEQUALTO -> {
                    filterList.add(new IsEqualTo(info.getKey(),info.getValue()));
                }
                case ISLESSTHAN -> {
                    // 需要单独处理
                }
                case ISNOTEQUALTO -> {
                    filterList.add(new IsNotEqualTo(info.getKey(),Arrays.asList(info.getValue())));
                }
                case ISGREATERTHAN -> {
                    // 需要单独处理
                }
                case CONTAINSSTRING -> {
                    // 需要单独处理
                }
                case ISLESSTHANOREQUALTO -> {
                    // 需要单独处理
                }
                case ISGREATERTHANOREQUALTO ->{
                    // 需要单独处理
                }
            }
        }
        return filterList;
    }
}

