package com.simplechat.repository;

public interface CacheRepository {

    void addAuthKeyForUser(String userId, String authKey);

    String getUserIdByAuthKey(String authKey);

    /**
     * store activation code for given mobile
     * @param mobile
     * @param activationCode
     */
    void storeActivationCodeForMobile(String mobile, String activationCode);

    /**
     * get info by activation code and mobile
     * @param mobile
     * @param activationCode
     * @return
     */
    String getInfoByActivationCodeAndMobile(String mobile, String code);
}
