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

import com.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.ckfinder.connector.data.EventArgs;
import com.ckfinder.connector.data.EventCommandData;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.data.InitCommandEventArgs;
import com.ckfinder.connector.data.PluginInfo;
import com.ckfinder.connector.errors.ConnectorException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides support for event handlers.
 */
public class Events {

    private final List<EventCommandData<BeforeExecuteCommandEventArgs>> beforeExecuteCommandEventHandlers;
    private final List<EventCommandData<AfterFileUploadEventArgs>> afterFileUploadEventHandlers;
    private final List<EventCommandData<InitCommandEventArgs>> initCommandEventHandlers;

    /**
     * default constructor.
     */
    public Events() {
        this.beforeExecuteCommandEventHandlers = new ArrayList<>(6);
        this.afterFileUploadEventHandlers = new ArrayList<>(6);
        this.initCommandEventHandlers = new ArrayList<>(6);
    }

    /**
     * register events handlers for event.
     *
     * @param eventHandler event class to register
     */
    public void addBeforeExecuteEventHandler(Class<? extends IEventHandler<BeforeExecuteCommandEventArgs>> eventHandler) {
        beforeExecuteCommandEventHandlers.add(new EventCommandData<>(eventHandler));
    }

    public void addAfterFileUploadEventHandler(Class<? extends IEventHandler<AfterFileUploadEventArgs>> eventHandler) {
        afterFileUploadEventHandlers.add(new EventCommandData<>(eventHandler));
    }

    public void addInitCommandEventHandler(Class<? extends IEventHandler<InitCommandEventArgs>> eventHandler) {
        initCommandEventHandlers.add(new EventCommandData<>(eventHandler));
    }

    /**
     * register events handlers for event.
     *
     * @param eventHandler event class to register
     * @param pluginInfo plugin info
     */
    public void addBeforeExecuteEventHandler(Class<? extends IEventHandler<BeforeExecuteCommandEventArgs>> eventHandler,
            final PluginInfo pluginInfo) {
        beforeExecuteCommandEventHandlers.add(new EventCommandData<>(eventHandler, pluginInfo));
    }

    public void addAfterFileUploadEventHandler(Class<? extends IEventHandler<AfterFileUploadEventArgs>> eventHandler,
            final PluginInfo pluginInfo) {
        afterFileUploadEventHandlers.add(new EventCommandData<>(eventHandler, pluginInfo));
    }

    public void addInitCommandEventHandler(Class<? extends IEventHandler<InitCommandEventArgs>> eventHandler,
            final PluginInfo pluginInfo) {
        initCommandEventHandlers.add(new EventCommandData<>(eventHandler, pluginInfo));
    }

    /**
     * run event handlers for selected event.
     *
     * @param args event execute arguments.
     * @param configuration connector configuration
     * @return false when end executing command.
     * @throws ConnectorException when error occurs.
     */
    public boolean runBeforeExecuteCommand(BeforeExecuteCommandEventArgs args, IConfiguration configuration)
            throws ConnectorException {
        return run(beforeExecuteCommandEventHandlers, args, configuration);
    }

    public boolean runAfterFileUpload(AfterFileUploadEventArgs args, IConfiguration configuration)
            throws ConnectorException {
        return run(afterFileUploadEventHandlers, args, configuration);
    }

    public boolean runInitCommand(InitCommandEventArgs args, IConfiguration configuration)
            throws ConnectorException {
        return run(initCommandEventHandlers, args, configuration);
    }

    private <T extends EventArgs> boolean run(List<EventCommandData<T>> handlers, T args, IConfiguration configuration)
            throws ConnectorException {
        for (EventCommandData<T> eventCommandData : handlers) {
            try {
                IEventHandler<T> events;
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

}
