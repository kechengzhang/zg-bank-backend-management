package com.vip.file.service.strategy;

/**
 * @author zkc
 */
public class UserActionRecordFactory {
    public static IStrategy getIStrategy(Integer type) {
        switch (type) {
            case 1:
                return new SaveFileVerificationRules();
            case 2:
                return new UpdateFileVerificationRules();
            case 3:
                return new DeleteFileVerificationRules();
            case 4:
                return new DeleteSyntheticVideo();
            case 5:
                return new UpdatePassword();
            case 6:
                return new UserLogin();
            case 7:
                return new UploadFile();
            case 8:
                return new DeleteSplitVideo();
            default:
                return null;
        }
    }
}