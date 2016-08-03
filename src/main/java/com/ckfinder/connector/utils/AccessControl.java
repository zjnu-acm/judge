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
package com.ckfinder.connector.utils;

import com.ckfinder.connector.configuration.IConfiguration;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to generate ACL values.
 */
@SuppressWarnings("FinalClass")
public final class AccessControl {

    /**
     * Folder view mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_VIEW = 1;
    /**
     * Folder create mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_CREATE = 1 << 1;
    /**
     * Folder rename mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_RENAME = 1 << 2;
    /**
     * Folder delete mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_DELETE = 1 << 3;
    /**
     * File view mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_VIEW = 1 << 4;
    /**
     * File upload mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_UPLOAD = 1 << 5;
    /**
     * File rename mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_RENAME = 1 << 6;
    /**
     * File delete mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_DELETE = 1 << 7;

    /**
     * acl configuration.
     */
    private final List<ACLEntry> aclEntries;
    /**
     * connector configuration.
     */
    private final IConfiguration configuration;

    public AccessControl(IConfiguration configuration) {
        this.configuration = configuration;
        this.aclEntries = loadACLConfig();
    }

    /**
     * check ACL for folder.
     *
     * @param resourceType resource type name
     * @param folder folder name
     * @param acl acl to check.
     * @param currentUserRole user role
     * @return true if acl flag is true
     */
    public boolean checkFolderACL(String resourceType,
            String folder, String currentUserRole,
            int acl) {
        return (checkACLForRole(resourceType, folder, currentUserRole) & acl) == acl;
    }

    /**
     * Checks ACL for given role.
     *
     * @param resourceType resource type
     * @param folder current folder
     * @param currentUserRole current user role
     * @return acl value
     */
    public int checkACLForRole(String resourceType, String folder,
            String currentUserRole) {
        CheckEntry[] ce = new CheckEntry[currentUserRole != null ? 4 : 2];

        ce[0] = new CheckEntry("*", "*");
        ce[1] = new CheckEntry("*", resourceType);

        if (currentUserRole != null) {
            ce[2] = new CheckEntry(currentUserRole, "*");
            ce[3] = new CheckEntry(currentUserRole, resourceType);
        }

        int acl = 0;
        for (CheckEntry checkEntry : ce) {
            List<ACLEntry> aclEntrieForType = findACLEntryByRoleAndType(checkEntry.type,
                    checkEntry.role);

            for (ACLEntry aclEntry : aclEntrieForType) {
                String cuttedPath = folder;

                while (true) {
                    if (cuttedPath.length() > 1
                            && cuttedPath.lastIndexOf('/') == cuttedPath.length() - 1) {
                        cuttedPath = cuttedPath.substring(0, cuttedPath.length() - 1);
                    }
                    if (aclEntry.folder.equals(cuttedPath)) {
                        acl = checkACLForFolder(aclEntry, cuttedPath);
                        break;
                    } else if (cuttedPath.length() == 1) {
                        break;
                    } else if (cuttedPath.lastIndexOf('/') > -1) {
                        cuttedPath = cuttedPath.substring(0,
                                cuttedPath.lastIndexOf('/') + 1);
                    } else {
                        break;
                    }
                }
            }
        }
        return acl;
    }

    /**
     * Caches ACL configuration from connector's configuration in order to
     * avoid. any unnecessary overhead related with reading/writing ACL's on
     * every request.
     */
    private List<ACLEntry> loadACLConfig() {
        return this.configuration.getAccessConrolLevels().stream().map(item -> {
            ACLEntry aclEntry = new ACLEntry();
            aclEntry.role = item.getRole();
            aclEntry.type = item.getResourceType();
            aclEntry.folder = item.getFolder();
            aclEntry.fileDelete = item.isFileDelete();
            aclEntry.fileRename = item.isFileRename();
            aclEntry.fileUpload = item.isFileUpload();
            aclEntry.fileView = item.isFileView();
            aclEntry.folderCreate = item.isFolderCreate();
            aclEntry.folderDelete = item.isFolderDelete();
            aclEntry.folderRename = item.isFolderRename();
            aclEntry.folderView = item.isFolderView();
            return aclEntry;
        }).collect(Collectors.toList());
    }

    /**
     * Checks ACL for given folder.
     *
     * @param entry current ACL entry
     * @param folder current folder
     * @return acl value
     */
    private int checkACLForFolder(ACLEntry entry, String folder) {
        int acl = 0;
        if (folder.contains(entry.folder) || entry.folder.equals(File.separator)) {
            acl ^= entry.countACL();
        }
        return acl;
    }

    /**
     * Gets a list of ACL entries for current role and resource type.
     *
     * @param type resource type
     * @param role current user role
     * @return list of ACL entries.
     */
    private List<ACLEntry> findACLEntryByRoleAndType(String type,
            String role) {
        return aclEntries.stream()
                .filter(item -> (item.role.equals(role) && item.type.equals(type)))
                .collect(Collectors.toList());
    }

    /**
     * Simple ACL entry class.
     */
    private static class ACLEntry {

        /**
         * role name.
         */
        private String role;
        /**
         * resource type name.
         */
        private String type;
        /**
         * folder name.
         */
        private String folder;
        /**
         * folder view flag.
         */
        private boolean folderView;
        /**
         * folder create flag.
         */
        private boolean folderCreate;
        /**
         * folder rename flag.
         */
        private boolean folderRename;
        /**
         * folder delete flag.
         */
        private boolean folderDelete;
        /**
         * file view flag.
         */
        private boolean fileView;
        /**
         * file upload flag.
         */
        private boolean fileUpload;
        /**
         * file rename flag.
         */
        private boolean fileRename;
        /**
         * file delete flag.
         */
        private boolean fileDelete;

        /**
         * count entry ACL.
         *
         * @return entry acl
         */
        private int countACL() {
            int acl = 0;
            acl |= (folderView) ? CKFINDER_CONNECTOR_ACL_FOLDER_VIEW : 0;
            acl |= (folderCreate) ? CKFINDER_CONNECTOR_ACL_FOLDER_CREATE : 0;
            acl |= (folderRename) ? CKFINDER_CONNECTOR_ACL_FOLDER_RENAME : 0;
            acl |= (folderDelete) ? CKFINDER_CONNECTOR_ACL_FOLDER_DELETE : 0;

            acl |= (fileView) ? CKFINDER_CONNECTOR_ACL_FILE_VIEW : 0;
            acl |= (fileUpload) ? CKFINDER_CONNECTOR_ACL_FILE_UPLOAD : 0;
            acl |= (fileRename) ? CKFINDER_CONNECTOR_ACL_FILE_RENAME : 0;
            acl |= (fileDelete) ? CKFINDER_CONNECTOR_ACL_FILE_DELETE : 0;
            return acl;
        }

        @Override
        public String toString() {
            return role + " " + type + " " + folder;
        }
    }

    /**
     * simple check ACL entry.
     */
    private static class CheckEntry {

        private String role;
        private String type;

        /**
         * Constructor.
         *
         * @param role current user role.
         * @param type resource type.
         */
        public CheckEntry(String role, String type) {
            this.role = role;
            this.type = type;
        }
    }

}
