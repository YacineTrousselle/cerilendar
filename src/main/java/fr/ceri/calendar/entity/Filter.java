package fr.ceri.calendar.entity;

public record Filter(FilterType type, String value) {

    public enum FilterType {
        LOCATION("Salle"),
        COURSE("Cours"),
        TEACHER("Professeur"),
        TYPE("Type"),
        ;

        private final String value;
        FilterType(String type) {
            value = type;
        }

        public String getValue() {
            return value;
        }

        public static FilterType fromString(String value) {
            for (FilterType type : FilterType.values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return null;
        }
    }
}
