package com.appdev.terra.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum QualificationsEnum {
    FOOD,
    HYDRATION,
    HUMAN_FORCE,
    HEAVY_MACHINERY,
    TRANSPORT,
    MENTAL_HELP,
    SHELTER;


    public static String encodeQualifications(HashSet<QualificationsEnum> qualifications) {
        return Arrays.stream(QualificationsEnum.values())
                .map((q) -> {return qualifications.contains(q) ? "T" : "F";})
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String encodeQualifications(ArrayList<Boolean> qualifications) {
        return qualifications.stream()
                             .map((q) -> {return q ? "T" : "F";})
                             .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                             .toString();
    }

    public static ArrayList<Boolean> encodeQualificationsBools(HashSet<QualificationsEnum> qualifications) {
        return Arrays.stream(QualificationsEnum.values())
                     .map(qualifications::contains)
                     .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static HashSet<QualificationsEnum> decodeQualifications(String qualifications) {
        HashSet<QualificationsEnum> qualificationsSet = new HashSet<>();
        int i = 0;

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            if (i == qualifications.length()) {
                break;
            }
            if (qualifications.charAt(i++) == 'T') {
                qualificationsSet.add(qualification);
            }
        }

        return qualificationsSet;
    }
};