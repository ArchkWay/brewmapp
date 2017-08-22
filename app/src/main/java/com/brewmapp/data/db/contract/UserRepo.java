package com.brewmapp.data.db.contract;

import com.brewmapp.data.entity.User;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

/**
 * Created by oleg on 25.07.17.
 */

public interface UserRepo extends Repo<User>, IdentityProvider {
}
