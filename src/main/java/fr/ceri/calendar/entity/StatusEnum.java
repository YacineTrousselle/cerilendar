package fr.ceri.calendar.entity;

public enum StatusEnum {
    TEACHER {
        @Override
        public String toFrenchString() {
            return "Professeur";
        }
    },
    STUDENT {
        @Override
        public String toFrenchString() {
            return "Etudiant";
        }
    };

    public abstract String toFrenchString();
}
