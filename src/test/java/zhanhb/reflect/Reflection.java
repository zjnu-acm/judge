/*
 * Copyright (C) 2014 zhanhb
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package zhanhb.reflect;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class Reflection {

    public static Class<?> getCallerClass() {
        return SecurityContext.INSTANCE.getCallerClass(2);
    }

    @Deprecated
    public static Class<?> getCallerClass(int i) {
        return SecurityContext.INSTANCE.getCallerClass(i);
    }

    private static class SecurityContext extends SecurityManager {

        static SecurityContext INSTANCE = AccessController.doPrivileged((PrivilegedAction<SecurityContext>) SecurityContext::new);

        private SecurityContext() {
        }

        Class<?> getCallerClass(int i) {
            if (i >= 0) {
                try {
                    return getClassContext()[i + 1];
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
            return null;
        }
    }
}
