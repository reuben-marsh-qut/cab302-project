package com.example.cab302project.model.enums;

public enum Category {
        MIND("Mind"),
        BODY("Body"),
        SOCIAL("Social");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
                return label;
        }

        @Override
        public String toString() {
                return this.label;
        }
}
