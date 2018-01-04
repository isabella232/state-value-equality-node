/*
 *
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 David Luna.
 *
 */

package com.luna.authentication;

import java.util.Collection;
import java.util.Collections;

/**
 * For comparing values of objects and Collections.
 */
public class Matcher {

    Matcher() { }

    /**
     * Returns true if the provided Objects are the same, or if they are both null.
     *
     * If one of the Objects is a Collection, returns true if it's size is one, and its first value is equal to
     * the other Object.
     *
     * If both of the Objects are Collections, returns true if all elements in both sets are equal (regardless of
     * order).
     *
     * @param one An object to compare.
     * @param two An object to compare.
     * @return true or false.
     */
    public boolean matchAll(Object one, Object two) {
        Boolean initial = checkNulls(one, two);

        if (initial == null) {
            return internalMatchAll(one, two);
        } else {
            return initial;
        }
    }

    private boolean internalMatchAll(Object one, Object two) {
        if (one instanceof Collection && two instanceof Collection) {
            Collection oneC = (Collection) one;
            Collection twoC = (Collection) two;
            return oneC.size() == twoC.size() && oneC.containsAll(twoC);
        } else if (one instanceof Collection) {
            Collection oneC = (Collection) one;
            return oneC.size() == 1 && oneC.iterator().next().equals(two);
        } else if (two instanceof Collection) {
            Collection twoC = (Collection) two;
            return twoC.size() == 1 && twoC.iterator().next().equals(two);
        } else {
            return one.equals(two);
        }
    }

    /**
     * Returns true if the provided Objects are exactly the same, or if they are both null.
     *
     * Returns true if the first object's .equals() method returns true when passed the second object.
     *
     * @param one An object to compare.
     * @param two An object to compare.
     * @return true or false.
     */
    public boolean matchExact(Object one, Object two) {
        Boolean initial = checkNulls(one, two);

        if (initial == null) {
            return internalMatchExact(one, two);
        } else {
            return initial;
        }
    }

    private boolean internalMatchExact(Object one, Object two) {
        return one.equals(two);
    }

    /**
     * Returns true if the provided Objects are the same, or if they are both null.
     *
     * If one of the Objects is a Collection, returns true if its first value is equal to the other Object.
     *
     * If both of the Objects are Collections, returns true if the first value of each Object is equal.
     *
     * @param one An object to compare.
     * @param two An object to compare.
     * @return true or false.
     */
    public boolean matchFirst(Object one, Object two) {
        Boolean initial = checkNulls(one, two);

        if (initial == null) {
            return internalMatchFirst(one, two);
        } else {
            return initial;
        }
    }

    private boolean internalMatchFirst(Object one, Object two) {
        if (one instanceof Collection && two instanceof Collection) {
            Collection oneC = (Collection) one;
            Collection twoC = (Collection) two;

            return oneC.iterator().next().equals(twoC.iterator().next());
        } else if (one instanceof Collection) {
            Collection oneC = (Collection) one;
            return oneC.iterator().next().equals(two);
        } else if (two instanceof Collection) {
            Collection twoC = (Collection) two;
            return twoC.iterator().next().equals(one);
        } else {
            return one.equals(two);
        }
    }

    /**
     * Returns true if the provided Objects are the same, or if they are both null.
     *
     * If one of the Objects is a Collection, returns true if any of its values is equal to the other Object.
     *
     * If both of the Objects are Collections, returns true if any of its values equals any of the other Collections'
     * values.
     *
     * @param one An object to compare.
     * @param two An object to compare.
     * @return true or false.
     */
    public boolean matchAny(Object one, Object two) {
        Boolean initial = checkNulls(one, two);

        if (initial == null) {
            return internalMatchAny(one, two);
        } else {
            return initial;
        }
    }

    private boolean internalMatchAny(Object one, Object two) {
        if (one instanceof Collection && two instanceof Collection) {
            Collection oneC = (Collection) one;
            Collection twoC = (Collection) two;

            return (!Collections.disjoint(oneC, twoC));
        } else if (one instanceof Collection) {
            Collection oneC = (Collection) one;
            return oneC.contains(two);
        } else if (two instanceof Collection) {
            Collection twoC = (Collection) two;
            return twoC.contains(one);
        } else {
            return one.equals(two);
        }
    }

    private Boolean checkNulls(Object one, Object two) {

        if (one == null && two == null) {
            return true;
        } else if (one == null || two == null) {
            return false;
        }

        return null;

    }

}
