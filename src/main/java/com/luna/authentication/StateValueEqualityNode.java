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

import com.google.inject.assistedinject.Assisted;
import com.sun.identity.sm.RequiredValueValidator;

import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.*;

import javax.inject.Inject;

import java.util.Collections;
import java.util.Map;

/** 
 * A node that checks for equality between values in a user's authentication shared state.
 */
@Node.Metadata(outcomeProvider  = AbstractDecisionNode.OutcomeProvider.class,
               configClass      = StateValueEqualityNode.Config.class)
public class StateValueEqualityNode extends AbstractDecisionNode {

    private final Config config;
    private final Matcher matcher;

    /**
     * Configuration for the node.
     */
    public interface Config {
        @Attribute(order = 100, validators = {RequiredValueValidator.class})
        default Map<String, String> keys() { return Collections.emptyMap(); }

        @Attribute(order = 200, validators = {RequiredValueValidator.class})
        default MatchType matchType() { return MatchType.MatchFirst; }

        @Attribute(order = 300, validators = {RequiredValueValidator.class})
        default OperationMode operationMode() { return OperationMode.AllTrue; }
    }

    /**
     * Create the node.
     *
     * @param config The service config.
     * @param matcher The matcher-helping class.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public StateValueEqualityNode(@Assisted Config config, Matcher matcher) throws NodeProcessException {
        this.config = config;
        this.matcher = matcher;
    }

    @Override
    public Action process(TreeContext context) throws NodeProcessException {

        boolean result = false;

        for (Map.Entry<String, String> entries : config.keys().entrySet()) {
            Object one = getValueForKey(entries.getKey(), context);
            Object two = getValueForKey(entries.getValue(), context);

            switch (config.matchType()) {
                case MatchAny:
                    result = matcher.matchAny(one, two);
                    break;
                case MatchExact:
                    result = matcher.matchExact(one, two);
                    break;
                case MatchAll:
                    result = matcher.matchAll(one, two);
                    break;
                case MatchFirst:
                default:
                    result = matcher.matchFirst(one, two);
            }

            if (config.operationMode() == OperationMode.AllTrue) {
                if (!result) {
                    return goTo(false).build();
                }
            } else {
                if (result) {
                    return goTo(true).build();
                }
            }
        }

        return goTo(result).build();
    }

    private Object getValueForKey(String key, TreeContext context) {
        return context.sharedState.get(key).getObject();
    }

    /**
     * Enum representing various matching approaches.
     */
    public enum MatchType {
        MatchAny,
        MatchFirst,
        MatchExact,
        MatchAll
    }

    /**
     * Enum representing various operation modes.
     */
    public enum OperationMode
    {
        AllTrue,
        OneTrue
    }
}