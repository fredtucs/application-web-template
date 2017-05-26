package org.wifry.fooddelivery.repository.security;

import org.springframework.stereotype.Repository;
import org.wifry.fooddelivery.model.LoginLog;
import org.wifry.fooddelivery.base.BaseRepository;

/**
 * Created by Heidi on 16/11/2015.
 *
 * LoginDao
 */
@Repository
public interface LoginRepository extends BaseRepository<LoginLog, Long> {
}
