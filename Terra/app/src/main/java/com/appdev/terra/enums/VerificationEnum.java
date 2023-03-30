package com.appdev.terra.enums;

public enum VerificationEnum {
    VERIFIED,
    AWAITING_VERIFICATION,

    // This last option is commented out because we might as well yeet the post collection if the
    // post collection is to be considered rejected.
    // REJECTED
}
