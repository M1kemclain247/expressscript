package com.m1kes.expressscript.objects;

public enum MenuId {




    REQUEST_QUOTES_MENU(false),
    ORDERS_MENU(false),
    QUOTES_MENU(false),
    HEALTH_TIPS_MENU(false),
    BRANCH_LOCATOR(false),
    CONTACT_US_MENU(false),
    LANDING_MAIN_MENU(false);


    final boolean validate;

    MenuId(final boolean validate) {
        this.validate = validate;
    }

    public boolean isValidate() {
        return validate;
    }
}


