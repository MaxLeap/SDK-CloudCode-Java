package myApp.hook;

import as.leap.code.EntityManager;
import as.leap.code.impl.ZEntityManagerHookBase;
import myApp.entity.User;

/**
 *
 */
@EntityManager("User")
public class UserManagerHook extends ZEntityManagerHookBase<User> {

}
