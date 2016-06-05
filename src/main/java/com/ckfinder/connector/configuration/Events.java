/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.ckfinder.connector.configuration;

import com.ckfinder.connector.data.EventArgs;
import com.ckfinder.connector.data.EventCommandData;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.data.PluginInfo;
import com.ckfinder.connector.errors.ConnectorException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Provides support for event handlers.
 */
public class Events {

    private final Map<EventTypes, List<EventCommandData>> eventHandlers;

    /**
     * default constructor.
     */
    public Events() {
        eventHandlers = new EnumMap<>(EventTypes.class);
        eventHandlers.put(EventTypes.AfterFileUpload, new ArrayList<>(6));
        eventHandlers.put(EventTypes.InitCommand, new ArrayList<>(6));
        eventHandlers.put(EventTypes.BeforeExecuteCommand, new ArrayList<>(6));
    }

    /**
     * register events handlers for event.
     *
     * @param event selected event
     * @param eventHandler event class to register
     */
    public void addEventHandler(final EventTypes event,
            final Class<? extends IEventHandler> eventHandler) {
        EventCommandData eventCommandData = new EventCommandData(eventHandler);
        eventHandlers.get(event).add(eventCommandData);
    }

    /**
     * register events handlers for event.
     *
     * @param event selected event
     * @param eventHandler event class to register
     * @param pluginInfo plugin info
     */
    public void addEventHandler(final EventTypes event,
            final Class<? extends IEventHandler> eventHandler,
            final PluginInfo pluginInfo) {
        EventCommandData eventCommandData = new EventCommandData(eventHandler);
        eventCommandData.setPluginInfo(pluginInfo);
        eventHandlers.get(event).add(eventCommandData);
    }

    /**
     * run event handlers for selected event.
     *
     * @param eventTyp selected event.
     * @param args event execute arguments.
     * @param configuration connector configuration
     * @return false when end executing command.
     * @throws ConnectorException when error occurs.
     */
    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    public boolean run(final EventTypes eventTyp, final EventArgs args,
            final IConfiguration configuration)
            throws ConnectorException {
        for (EventCommandData eventCommandData : eventHandlers.get(eventTyp)) {
            try {
                IEventHandler events;
                if (eventCommandData.getPluginInfo() != null) {
                    events = eventCommandData.getEventListener().getConstructor(PluginInfo.class).newInstance(eventCommandData.getPluginInfo());
                } else {
                    events = eventCommandData.getEventListener().newInstance();
                }
                if (!events.runEventHandler(args, configuration)) {
                    return false;
                }
            } catch (ConnectorException ex) {
                throw ex;
            } catch (Exception e) {
                throw new ConnectorException(e);
            }

        }
        return true;
    }

    /**
     * The list of events.
     */
    @SuppressWarnings("PublicInnerClass")
    public enum EventTypes {

        BeforeExecuteCommand, AfterFileUpload, InitCommand;

    }
}
