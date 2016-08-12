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
package com.github.zhanhb.ckfinder.connector.configuration;

import com.github.zhanhb.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.github.zhanhb.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.github.zhanhb.ckfinder.connector.data.EventArgs;
import com.github.zhanhb.ckfinder.connector.data.IEventHandler;
import com.github.zhanhb.ckfinder.connector.data.InitCommandEventArgs;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import java.util.List;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides support for event handlers.
 */
@Builder(builderClassName = "Builder")
@Slf4j
public class Events {

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private static <T extends EventArgs> boolean run(List<Supplier<? extends IEventHandler<T>>> handlers, T args, IConfiguration configuration) throws ConnectorException {
        log.trace("{}", handlers);
        for (Supplier<? extends IEventHandler<T>> eventCommandData : handlers) {
            try {
                IEventHandler<T> events = eventCommandData.get();
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

    @Singular
    private final List<Supplier<? extends IEventHandler<BeforeExecuteCommandEventArgs>>> beforeExecuteCommandEventHandlers;
    @Singular
    private final List<Supplier<? extends IEventHandler<AfterFileUploadEventArgs>>> afterFileUploadEventHandlers;
    @Singular
    private final List<Supplier<? extends IEventHandler<InitCommandEventArgs>>> initCommandEventHandlers;

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

    public boolean runInitCommand(InitCommandEventArgs args, IConfiguration configuration) {
        try {
            return run(initCommandEventHandlers, args, configuration);
        } catch (ConnectorException ex) {
            // impossible
            throw new AssertionError(ex);
        }
    }

}
