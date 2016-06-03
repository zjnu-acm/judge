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
package com.ckfinder.connector;

import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.configuration.ConfigurationFactory;
import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.Events.EventTypes;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.handlers.command.Command;
import com.ckfinder.connector.handlers.command.CopyFilesCommand;
import com.ckfinder.connector.handlers.command.CreateFolderCommand;
import com.ckfinder.connector.handlers.command.DeleteFilesCommand;
import com.ckfinder.connector.handlers.command.DeleteFolderCommand;
import com.ckfinder.connector.handlers.command.DownloadFileCommand;
import com.ckfinder.connector.handlers.command.ErrorCommand;
import com.ckfinder.connector.handlers.command.FileUploadCommand;
import com.ckfinder.connector.handlers.command.GetFilesCommand;
import com.ckfinder.connector.handlers.command.GetFoldersCommand;
import com.ckfinder.connector.handlers.command.IPostCommand;
import com.ckfinder.connector.handlers.command.InitCommand;
import com.ckfinder.connector.handlers.command.MoveFilesCommand;
import com.ckfinder.connector.handlers.command.QuickUploadCommand;
import com.ckfinder.connector.handlers.command.RenameFileCommand;
import com.ckfinder.connector.handlers.command.RenameFolderCommand;
import com.ckfinder.connector.handlers.command.ThumbnailCommand;
import com.ckfinder.connector.handlers.command.XMLCommand;
import com.ckfinder.connector.handlers.command.XMLErrorCommand;
import com.ckfinder.connector.utils.AccessControlUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Main connector servlet for handling CKFinder requests.
 */
@Slf4j
public class ConnectorServlet extends HttpServlet {

    /**
     */
    private static final long serialVersionUID = 2960665641425153638L;
    /**
     * holds exception if any occurs during CKFinder start.
     */
    private Exception startException;

    /**
     * Handling get requests.
     *
     * @param request request
     * @param response response
     * @throws IOException .
     * @throws ServletException .
     */
    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        getResponse(request, response, false);
    }

    /**
     * Handling post requests.
     *
     * @param request request
     * @param response response
     * @throws IOException .
     * @throws ServletException .
     */
    @Override
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        getResponse(request, response, true);
    }

    /**
     * Creating response for every command in request parameter.
     *
     * @param request request
     * @param response response
     * @param post if it's post command.
     * @throws ServletException when error occurs.
     */
    private void getResponse(final HttpServletRequest request,
            final HttpServletResponse response, final boolean post)
            throws ServletException {
        if (startException != null
                && Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
            throw new ServletException(startException);
        }
        boolean isNativeCommand;
        String command = request.getParameter("command");
        IConfiguration configuration = null;
        try {
            configuration = ConfigurationFactory.getInstace().getConfiguration(request);
            if (configuration == null) {
                throw new Exception("Configuration wasn't initialized correctly. Check server logs.");
            }
        } catch (Exception e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                log.error("Configuration wasn't initialized correctly. Check server logs.", e);
            }
            throw new ServletException(e);
        }
        try {

            if (command == null || command.isEmpty()) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false);
            }

            configuration.setDebugMode(
                    Boolean.valueOf(getServletConfig().getInitParameter("debug")));

            if (CommandHandlerEnum.contains(command.toUpperCase())) {
                CommandHandlerEnum cmd;
                isNativeCommand = true;
                cmd = CommandHandlerEnum.valueOf(command.toUpperCase());
                // checks if command should go via POST request or it's a post request
                // and it's not upload command
                if ((IPostCommand.class.isAssignableFrom(cmd.getCommandClass()) || post)
                        && !CommandHandlerEnum.FILEUPLOAD.equals(cmd)
                        && !CommandHandlerEnum.QUICKUPLOAD.equals(cmd)) {
                    checkPostRequest(request);
                }
            } else {
                isNativeCommand = false;
            }

            BeforeExecuteCommandEventArgs args = new BeforeExecuteCommandEventArgs();
            args.setCommand(command);
            args.setRequest(request);
            args.setResponse(response);

            if (configuration.getEvents() != null) {
                if (configuration.getEvents().run(EventTypes.BeforeExecuteCommand,
                        args, configuration)) {
                    if (!isNativeCommand) {
                        command = null;
                    }
                    executeNativeCommand(command, request, response, configuration, isNativeCommand);
                }
            } else {
                if (!isNativeCommand) {
                    command = null;
                }
                executeNativeCommand(command, request, response, configuration, isNativeCommand);
            }
        } catch (IllegalArgumentException e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                log.error("Couldn't execute native command.", e);
                response.reset();
                throw new ServletException(e);
            } else {
                handleError(
                        new ConnectorException(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false),
                        configuration, request, response, command);
            }
        } catch (ConnectorException e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                log.error(e.getErrorMessage(), e.getException() != null ? e.getException() : e);
                response.reset();
                throw new ServletException(e.getException());
            } else {
                handleError(e, configuration, request, response, command);
            }
        }
    }

    /**
     * Executes one of connector's predefined commands specified as parameter.
     *
     * @param command string representing command name
     * @param request current request object
     * @param respose current response object
     * @param configuration CKFinder connector configuration
     * @param isNativeCommand flag indicating whether command is available in
     * enumeration object
     *
     * @throws ConnectorException when command isn't native
     * @throws IllegalArgumentException when provided command is not found in
     * enumeration object
     */
    private void executeNativeCommand(String command, final HttpServletRequest request,
            final HttpServletResponse response, IConfiguration configuration,
            boolean isNativeCommand) throws IllegalArgumentException, ConnectorException {
        if (isNativeCommand) {
            CommandHandlerEnum cmd = CommandHandlerEnum.valueOf(command.toUpperCase());
            cmd.execute(
                    request, response, configuration, getServletContext());
        } else {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false);
        }
    }

    /**
     * checks post request if it's ckfinder command.
     *
     * @param request request
     * @throws ConnectorException when param isn't set or has wrong value.
     */
    private void checkPostRequest(final HttpServletRequest request)
            throws ConnectorException {
        if (request.getParameter("CKFinderCommand") == null
                || !(request.getParameter("CKFinderCommand").equals("true"))) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, true);
        }

    }

    /**
     * handles error from execute command.
     *
     * @param e exception
     * @param request request
     * @param response response
     * @param configuration connector configuration
     * @param currentCommand current command
     * @throws ServletException when error handling fails.
     */
    private void handleError(final ConnectorException e,
            final IConfiguration configuration,
            final HttpServletRequest request, final HttpServletResponse response,
            final String currentCommand)
            throws ServletException {
        try {
            if (currentCommand != null && !currentCommand.isEmpty()) {
                Class<? extends Command> commandClass = CommandHandlerEnum.valueOf(
                        currentCommand.toUpperCase()).getCommandClass();
                if (XMLCommand.class.isAssignableFrom(commandClass)) {
                    CommandHandlerEnum.XMLERROR.execute(request, response, configuration,
                            getServletContext(), e);
                } else {
                    CommandHandlerEnum.ERROR.execute(request, response, configuration,
                            getServletContext(), e);
                }
            } else {
                CommandHandlerEnum.XMLERROR.execute(request, response, configuration,
                        getServletContext(), e);
            }

        } catch (Exception e1) {
            throw new ServletException(e1);
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void init() throws ServletException {
        ServletContextFactory.setServletContext(getServletContext());
        IConfiguration configuration;
        String className = getServletConfig().getInitParameter(
                "configuration");
        if (className != null) {
            try {
                Class<? extends IConfiguration> clazz = Class.forName(className).asSubclass(IConfiguration.class);

                try {
                    configuration = clazz.getConstructor(
                            ServletConfig.class).newInstance(getServletConfig());

                } catch (NoSuchMethodException ex) {
                    configuration = clazz.newInstance();
                }
            } catch (Exception e) {
                log.error("Couldn't initialize custom configuration. Rolling back to the default one.", e);
                configuration = new Configuration(getServletConfig());
            }
        } else {
            configuration = new Configuration(getServletConfig());
        }
        try {
            configuration.init();
            AccessControlUtil.getInstance().loadConfiguration(configuration);
        } catch (Exception e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                log.error("Couldn't initialize configuration object.", e);
            }
            this.startException = e;
            configuration = null;
        }
        ConfigurationFactory.getInstace().setConfiguration(configuration);
    }

    /**
     * Enum with all command handles by servlet.
     *
     */
    private enum CommandHandlerEnum {

        /**
         * init command.
         */
        INIT(InitCommand.class),
        /**
         * get subfolders for selected location command.
         */
        GETFOLDERS(GetFoldersCommand.class),
        /**
         * get files from current folder command.
         */
        GETFILES(GetFilesCommand.class),
        /**
         * get thumbnail for file command.
         */
        THUMBNAIL(ThumbnailCommand.class),
        /**
         * download file command.
         */
        DOWNLOADFILE(DownloadFileCommand.class),
        /**
         * create subfolder.
         */
        CREATEFOLDER(CreateFolderCommand.class),
        /**
         * rename file.
         */
        RENAMEFILE(RenameFileCommand.class),
        /**
         * rename folder.
         */
        RENAMEFOLDER(RenameFolderCommand.class),
        /**
         * delete folder.
         */
        DELETEFOLDER(DeleteFolderCommand.class),
        /**
         * copy files.
         */
        COPYFILES(CopyFilesCommand.class),
        /**
         * move files.
         */
        MOVEFILES(MoveFilesCommand.class),
        /**
         * delete files.
         */
        DELETEFILES(DeleteFilesCommand.class),
        /**
         * file upload.
         */
        FILEUPLOAD(FileUploadCommand.class),
        /**
         * quick file upload.
         */
        QUICKUPLOAD(QuickUploadCommand.class),
        /**
         * XML error command.
         */
        XMLERROR(XMLErrorCommand.class),
        /**
         * error command.
         */
        ERROR(ErrorCommand.class);
        /**
         * command class for enum field.
         */
        private final Class<? extends Command> commandClass;
        /**
         * {@code Set} holding enumeration values,
         */
        private static final Set<String> enumValues = Arrays.asList(CommandHandlerEnum.values())
                .stream().map(CommandHandlerEnum::name).collect(Collectors.toSet());

        /**
         * Enum constructor to set command.
         *
         * @param command1 command name
         */
        private CommandHandlerEnum(final Class<? extends Command> commandClass) {
            this.commandClass = commandClass;
        }

        /**
         * Checks whether enumeration object contains command name specified as
         * parameter.
         *
         * @param enumValue string representing command name to check
         *
         * @return {@code true} is command exists, {@code false} otherwise
         */
        public static boolean contains(String enumValue) {
            return enumValues.contains(enumValue);
        }

        /**
         * Executes command.
         *
         * @param request request
         * @param response response
         * @param configuration connector configuration
         * @param sc servletContext
         * @param params params for command.
         * @throws ConnectorException when error occurs
         */
        private void execute(final HttpServletRequest request,
                final HttpServletResponse response, final IConfiguration configuration,
                final ServletContext sc, final Object... params)
                throws ConnectorException {
            Command com = null;
            try {
                com = commandClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e1) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
            }
            if (com == null) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
            }
            com.runCommand(request, response, configuration, params);
        }

        /**
         * gets command.
         *
         * @return command
         */
        @Deprecated
        public Command getCommand() {
            throw new NoSuchMethodError();
        }

        public Class<? extends Command> getCommandClass() {
            return commandClass;
        }

    }

}
